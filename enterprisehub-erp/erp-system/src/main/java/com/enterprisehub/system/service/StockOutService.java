package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockOut;

/**
 * 出库单服务接口
 */
public interface StockOutService extends IService<StockOut> {
    
    /**
     * 分页查询出库单列表
     */
    PageResult<StockOut> selectPage(PageQuery pageQuery);
    
    /**
     * 根据ID查询出库单详情
     */
    StockOut selectById(Long id);
    
    /**
     * 新增出库单
     */
    boolean insert(StockOut entity);
    
    /**
     * 修改出库单
     */
    boolean update(StockOut entity);
    
    /**
     * 删除出库单
     */
    boolean deleteById(Long id);
    
    /**
     * 审核出库单
     */
    int auditStockOut(Long id);
}
