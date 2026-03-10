package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.PurchaseOrder;
import com.enterprisehub.system.domain.entity.PurchaseOrderItem;
import com.enterprisehub.system.mapper.PurchaseOrderItemMapper;
import com.enterprisehub.system.mapper.PurchaseOrderMapper;
import com.enterprisehub.system.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 采购订单服务实现类
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderItemMapper itemMapper;

    @Override
    public PageResult<PurchaseOrder> selectPurchaseOrderList(PageQuery pageQuery, String orderNo, Integer status) {
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(orderNo), PurchaseOrder::getOrderNo, orderNo)
               .eq(status != null, PurchaseOrder::getStatus, status)
               .orderByDesc(PurchaseOrder::getCreateTime);
        
        Page<PurchaseOrder> page = this.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), wrapper);
        return PageResult.build(page);
    }

    @Override
    public PurchaseOrder selectPurchaseOrderById(Long id) {
        PurchaseOrder order = this.getById(id);
        if (order != null) {
            // 查询订单明细
            LambdaQueryWrapper<PurchaseOrderItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PurchaseOrderItem::getOrderId, id);
            List<PurchaseOrderItem> items = itemMapper.selectList(wrapper);
            order.setItems(items);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPurchaseOrder(PurchaseOrder order) {
        // 保存订单主表
        this.save(order);
        
        // 保存订单明细
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (PurchaseOrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePurchaseOrder(PurchaseOrder order) {
        // 更新订单主表
        this.updateById(order);
        
        // 删除原有明细
        LambdaQueryWrapper<PurchaseOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseOrderItem::getOrderId, order.getId());
        itemMapper.delete(wrapper);
        
        // 保存新明细
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (PurchaseOrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                itemMapper.insert(item);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePurchaseOrderById(Long id) {
        // 删除订单明细
        LambdaQueryWrapper<PurchaseOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseOrderItem::getOrderId, id);
        itemMapper.delete(wrapper);
        
        // 删除订单主表
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public int auditPurchaseOrder(Long id) {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(id);
        order.setStatus(1); // 已审核
        return this.updateById(order) ? 1 : 0;
    }

    @Override
    public int completePurchaseOrder(Long id) {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(id);
        order.setStatus(2); // 已完成
        return this.updateById(order) ? 1 : 0;
    }

    @Override
    public int closePurchaseOrder(Long id) {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(id);
        order.setStatus(3); // 已关闭
        return this.updateById(order) ? 1 : 0;
    }
}
