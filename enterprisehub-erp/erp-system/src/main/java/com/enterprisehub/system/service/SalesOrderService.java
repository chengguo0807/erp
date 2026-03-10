package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SalesOrder;

/**
 * 销售订单服务接口
 */
public interface SalesOrderService extends IService<SalesOrder> {
    
    /**
     * 分页查询销售订单列表
     */
    PageResult<SalesOrder> selectSalesOrderList(PageQuery pageQuery, String orderNo, Integer status);
    
    /**
     * 根据ID查询销售订单详情（含明细）
     */
    SalesOrder selectSalesOrderById(Long id);
    
    /**
     * 新增销售订单
     */
    int insertSalesOrder(SalesOrder order);
    
    /**
     * 修改销售订单
     */
    int updateSalesOrder(SalesOrder order);
    
    /**
     * 删除销售订单
     */
    int deleteSalesOrderById(Long id);
    
    /**
     * 审核销售订单
     */
    int auditSalesOrder(Long id);
    
    /**
     * 完成销售订单
     */
    int completeSalesOrder(Long id);
    
    /**
     * 关闭销售订单
     */
    int closeSalesOrder(Long id);
}
