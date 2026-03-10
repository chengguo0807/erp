package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.PurchaseOrder;
import com.enterprisehub.system.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 采购订单管理控制器
 */
@RestController
@RequestMapping("/purchase/v1/orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * 分页查询采购订单列表
     */
    @GetMapping
    public R<PageResult<PurchaseOrder>> list(PageQuery pageQuery, String orderNo, Integer status) {
        PageResult<PurchaseOrder> pageResult = purchaseOrderService.selectPurchaseOrderList(pageQuery, orderNo, status);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询采购订单详情
     */
    @GetMapping("/{id}")
    public R<PurchaseOrder> getInfo(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.selectPurchaseOrderById(id);
        return R.ok(order);
    }

    /**
     * 新增采购订单
     */
    @PostMapping
    @OperationLog(title = "采购订单", businessType = 1)
    public R<Void> add(@RequestBody PurchaseOrder order) {
        purchaseOrderService.insertPurchaseOrder(order);
        return R.ok();
    }

    /**
     * 修改采购订单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "采购订单", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        order.setId(id);
        purchaseOrderService.updatePurchaseOrder(order);
        return R.ok();
    }

    /**
     * 删除采购订单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "采购订单", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrderById(id);
        return R.ok();
    }

    /**
     * 审核采购订单
     */
    @PutMapping("/{id}/audit")
    @OperationLog(module = "采购订单", businessType = 1)
    public R<Void> audit(@PathVariable Long id) {
        purchaseOrderService.auditPurchaseOrder(id);
        return R.ok();
    }

    /**
     * 完成采购订单
     */
    @PutMapping("/{id}/complete")
    @OperationLog(title = "采购订单", businessType = 2)
    public R<Void> complete(@PathVariable Long id) {
        purchaseOrderService.completePurchaseOrder(id);
        return R.ok();
    }

    /**
     * 关闭采购订单
     */
    @PutMapping("/{id}/close")
    @OperationLog(module = "采购订单", businessType = 3)
    public R<Void> close(@PathVariable Long id) {
        purchaseOrderService.closePurchaseOrder(id);
        return R.ok();
    }
}
