package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SalesOrder;
import com.enterprisehub.system.domain.entity.SalesOrderItem;
import com.enterprisehub.system.mapper.SalesOrderItemMapper;
import com.enterprisehub.system.mapper.SalesOrderMapper;
import com.enterprisehub.system.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 销售订单服务实现类
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements SalesOrderService {

    @Autowired
    private SalesOrderItemMapper itemMapper;

    @Override
    public PageResult<SalesOrder> selectSalesOrderList(PageQuery pageQuery, String orderNo, Integer status) {
        LambdaQueryWrapper<SalesOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(orderNo), SalesOrder::getOrderNo, orderNo)
               .eq(status != null, SalesOrder::getStatus, status)
               .orderByDesc(SalesOrder::getCreateTime);
        
        Page<SalesOrder> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), wrapper);
        return PageResult.build(page);
    }

    @Override
    public SalesOrder selectSalesOrderById(Long id) {
        SalesOrder order = this.getById(id);
        if (order != null) {
            LambdaQueryWrapper<SalesOrderItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SalesOrderItem::getOrderId, id);
            List<SalesOrderItem> items = itemMapper.selectList(wrapper);
            order.setItems(items);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSalesOrder(SalesOrder order) {
        this.save(order);
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (SalesOrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSalesOrder(SalesOrder order) {
        this.updateById(order);
        LambdaQueryWrapper<SalesOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalesOrderItem::getOrderId, order.getId());
        itemMapper.delete(wrapper);
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (SalesOrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSalesOrderById(Long id) {
        LambdaQueryWrapper<SalesOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalesOrderItem::getOrderId, id);
        itemMapper.delete(wrapper);
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public int auditSalesOrder(Long id) {
        SalesOrder order = new SalesOrder();
        order.setId(id);
        order.setStatus(1); // 已审核
        return this.updateById(order) ? 1 : 0;
    }

    @Override
    public int completeSalesOrder(Long id) {
        SalesOrder order = new SalesOrder();
        order.setId(id);
        order.setStatus(2); // 已完成
        return this.updateById(order) ? 1 : 0;
    }

    @Override
    public int closeSalesOrder(Long id) {
        SalesOrder order = new SalesOrder();
        order.setId(id);
        order.setStatus(3); // 已关闭
        return this.updateById(order) ? 1 : 0;
    }
}
