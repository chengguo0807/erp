package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockIn;
import com.enterprisehub.system.domain.entity.StockInItem;
import com.enterprisehub.system.mapper.StockInItemMapper;
import com.enterprisehub.system.mapper.StockInMapper;
import com.enterprisehub.system.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 入库单服务实现类
 */
@Service
public class StockInServiceImpl extends ServiceImpl<StockInMapper, StockIn> implements StockInService {

    @Autowired
    private StockInItemMapper itemMapper;

    @Override
    public PageResult<StockIn> selectStockInList(PageQuery pageQuery, String stockInNo, Integer status) {
        LambdaQueryWrapper<StockIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(stockInNo), StockIn::getStockInNo, stockInNo)
               .eq(status != null, StockIn::getStatus, status)
               .orderByDesc(StockIn::getCreateTime);
        
        Page<StockIn> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), wrapper);
        return PageResult.build(page);
    }

    @Override
    public StockIn selectStockInById(Long id) {
        StockIn stockIn = this.getById(id);
        if (stockIn != null) {
            // 查询入库明细
            LambdaQueryWrapper<StockInItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StockInItem::getId, id);
            List<StockInItem> items = itemMapper.selectList(wrapper);
            stockIn.setItems(items);
        }
        return stockIn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertStockIn(StockIn stockIn) {
        // 保存入库单主表
        this.save(stockIn);
        
        // 保存入库明细
        if (stockIn.getItems() != null && !stockIn.getItems().isEmpty()) {
            for (StockInItem item : stockIn.getItems()) {
                item.setStockInId(stockIn.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStockIn(StockIn stockIn) {
        // 更新入库单主表
        this.updateById(stockIn);
        
        // 删除原有明细
        LambdaQueryWrapper<StockInItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockInItem::getId, stockIn.getId());
        itemMapper.delete(wrapper);
        
        // 保存新明细
        if (stockIn.getItems() != null && !stockIn.getItems().isEmpty()) {
            for (StockInItem item : stockIn.getItems()) {
                item.setStockInId(stockIn.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteStockInById(Long id) {
        // 删除入库明细
        LambdaQueryWrapper<StockInItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockInItem::getId, id);
        itemMapper.delete(wrapper);
        
        // 删除入库单主表
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public int auditStockIn(Long id) {
        StockIn stockIn = new StockIn();
        stockIn.setId(id);
        stockIn.setStatus(1); // 已审核
        return this.updateById(stockIn) ? 1 : 0;
    }
}
