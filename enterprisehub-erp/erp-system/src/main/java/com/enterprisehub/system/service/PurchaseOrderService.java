package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.PurchaseOrder;

/**
 * 采购订单服务接口
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    
    /**
     * 分页查询采购订单列表
     */
    PageResult<PurchaseOrder> selectPurchaseOrderList(PageQuery pageQuery, String orderNo, Integer status);
    
    /**
     * 根据ID查询采购订单详情（含明细）
     */
    PurchaseOrder selectPurchaseOrderById(Long id);
    
    /**
     * 新增采购订单
     */
    int insertPurchaseOrder(PurchaseOrder order);
    
    /**
     * 修改采购订单
     */
    int updatePurchaseOrder(PurchaseOrder order);
    
    /**
     * 删除采购订单
     */
    int deletePurchaseOrderById(Long id);
    
    /**
     * 审核采购订单
     */
    int auditPurchaseOrder(Long id);
    
    /**
     * 完成采购订单
     */
    int completePurchaseOrder(Long id);
    
    /**
     * 关闭采购订单
     */
    int closePurchaseOrder(Long id);
}
