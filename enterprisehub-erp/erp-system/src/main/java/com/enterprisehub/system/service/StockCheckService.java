package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.StockCheck;

/**
 * 库存盘点服务接口
 */
public interface StockCheckService extends IService<StockCheck> {
    
    /**
     * 分页查询盘点单列表
     */
    PageResult<StockCheck> selectPage(PageQuery pageQuery);
    
    /**
     * 根据ID查询盘点单详情
     */
    StockCheck selectById(Long id);
    
    /**
     * 新增盘点单
     */
    boolean insert(StockCheck entity);
    
    /**
     * 修改盘点单
     */
    boolean update(StockCheck entity);
    
    /**
     * 删除盘点单
     */
    boolean deleteById(Long id);
    
    /**
     * 审核盘点单
     */
    int auditStockCheck(Long id);
}
