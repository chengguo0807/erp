package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.SalesOrder;
import com.enterprisehub.system.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 销售订单管理控制器
 */
@RestController
@RequestMapping("/sales/v1/orders")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    /**
     * 分页查询销售订单列表
     */
    @GetMapping
    public R<PageResult<SalesOrder>> list(PageQuery pageQuery, String orderNo, Integer status) {
        PageResult<SalesOrder> pageResult = salesOrderService.selectSalesOrderList(pageQuery, orderNo, status);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询销售订单详情
     */
    @GetMapping("/{id}")
    public R<SalesOrder> getInfo(@PathVariable Long id) {
        SalesOrder order = salesOrderService.selectSalesOrderById(id);
        return R.ok(order);
    }

    /**
     * 新增销售订单
     */
    @PostMapping
    @OperationLog(title = "销售订单", businessType = 1)
    public R<Void> add(@RequestBody SalesOrder order) {
        salesOrderService.insertSalesOrder(order);
        return R.ok();
    }

    /**
     * 修改销售订单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "销售订单", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody SalesOrder order) {
        order.setId(id);
        salesOrderService.updateSalesOrder(order);
        return R.ok();
    }

    /**
     * 删除销售订单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "销售订单", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        salesOrderService.deleteSalesOrderById(id);
        return R.ok();
    }

    /**
     * 审核销售订单
     */
    @PutMapping("/{id}/audit")
    @OperationLog(module = "销售订单", businessType = 1)
    public R<Void> audit(@PathVariable Long id) {
        salesOrderService.auditSalesOrder(id);
        return R.ok();
    }

    /**
     * 完成销售订单
     */
    @PutMapping("/{id}/complete")
    @OperationLog(title = "销售订单", businessType = 2)
    public R<Void> complete(@PathVariable Long id) {
        salesOrderService.completeSalesOrder(id);
        return R.ok();
    }

    /**
     * 关闭销售订单
     */
    @PutMapping("/{id}/close")
    @OperationLog(module = "销售订单", businessType = 3)
    public R<Void> close(@PathVariable Long id) {
        salesOrderService.closeSalesOrder(id);
        return R.ok();
    }
}
