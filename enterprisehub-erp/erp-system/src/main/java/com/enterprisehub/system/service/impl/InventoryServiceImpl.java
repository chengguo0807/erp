package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.dto.InventoryTransferDTO;
import com.enterprisehub.system.domain.entity.Inventory;
import com.enterprisehub.system.mapper.InventoryMapper;
import com.enterprisehub.system.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存服务实现类
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Override
    public PageResult<Inventory> selectPage(PageQuery pageQuery, Long productId, Long warehouseId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(productId != null, Inventory::getProductId, productId)
               .eq(warehouseId != null, Inventory::getWarehouseId, warehouseId);
        Page<Inventory> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), wrapper);
        return PageResult.build(page);
    }

    @Override
    public Inventory selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(Inventory entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(Inventory entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<Inventory> getWarningList() {
        // 查询库存低于最小库存的商品
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.apply("quantity <= min_quantity");
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transfer(InventoryTransferDTO transferDTO) {
        // 查询源仓库库存
        LambdaQueryWrapper<Inventory> fromWrapper = new LambdaQueryWrapper<>();
        fromWrapper.eq(Inventory::getProductId, transferDTO.getProductId())
                   .eq(Inventory::getWarehouseId, transferDTO.getFromWarehouseId());
        Inventory fromInventory = this.getOne(fromWrapper);
        
        if (fromInventory == null || fromInventory.getQuantity().compareTo(transferDTO.getQuantity()) < 0) {
            throw new RuntimeException("源仓库库存不足");
        }
        
        // 扣减源仓库库存
        fromInventory.setQuantity(fromInventory.getQuantity().subtract(transferDTO.getQuantity()));
        this.updateById(fromInventory);
        
        // 查询目标仓库库存
        LambdaQueryWrapper<Inventory> toWrapper = new LambdaQueryWrapper<>();
        toWrapper.eq(Inventory::getProductId, transferDTO.getProductId())
                 .eq(Inventory::getWarehouseId, transferDTO.getToWarehouseId());
        Inventory toInventory = this.getOne(toWrapper);
        
        if (toInventory == null) {
            // 目标仓库没有该商品库存,创建新记录
            toInventory = new Inventory();
            toInventory.setProductId(transferDTO.getProductId());
            toInventory.setWarehouseId(transferDTO.getToWarehouseId());
            toInventory.setQuantity(transferDTO.getQuantity());
            toInventory.setMinQuantity(BigDecimal.ZERO);
            toInventory.setMaxQuantity(BigDecimal.ZERO);
            this.save(toInventory);
        } else {
            // 增加目标仓库库存
            toInventory.setQuantity(toInventory.getQuantity().add(transferDTO.getQuantity()));
            this.updateById(toInventory);
        }
        
        return 1;
    }
}
