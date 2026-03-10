package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockIn;

/**
 * 入库单服务接口
 */
public interface StockInService extends IService<StockIn> {
    
    /**
     * 分页查询入库单列表
     */
    PageResult<StockIn> selectStockInList(PageQuery pageQuery, String stockInNo, Integer status);
    
    /**
     * 根据ID查询入库单详情（含明细）
     */
    StockIn selectStockInById(Long id);
    
    /**
     * 新增入库单
     */
    int insertStockIn(StockIn stockIn);
    
    /**
     * 修改入库单
     */
    int updateStockIn(StockIn stockIn);
    
    /**
     * 删除入库单
     */
    int deleteStockInById(Long id);
    
    /**
     * 审核入库单
     */
    int auditStockIn(Long id);
}
