package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.InventoryTransaction;
import com.enterprisehub.system.mapper.InventoryTransactionMapper;
import com.enterprisehub.system.service.InventoryTransactionService;
import org.springframework.stereotype.Service;

@Service
public class InventoryTransactionServiceImpl extends ServiceImpl<InventoryTransactionMapper, InventoryTransaction> implements InventoryTransactionService {

    @Override
    public PageResult<InventoryTransaction> selectPage(PageQuery pageQuery, Long productId, Long warehouseId) {
        LambdaQueryWrapper<InventoryTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(productId != null, InventoryTransaction::getProductId, productId)
               .eq(warehouseId != null, InventoryTransaction::getWarehouseId, warehouseId)
               .orderByDesc(InventoryTransaction::getCreateTime);
        Page<InventoryTransaction> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<InventoryTransaction> result = this.page(page, wrapper);
        return PageResult.build(result);
    }

    @Override
    public InventoryTransaction selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(InventoryTransaction entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(InventoryTransaction entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
