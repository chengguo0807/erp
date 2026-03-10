package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Supplier;

/**
 * 供应商服务接口
 */
public interface SupplierService extends IService<Supplier> {
    
    /**
     * 分页查询供应商列表
     */
    PageResult<Supplier> selectSupplierList(PageQuery pageQuery, String supplierName);
    
    /**
     * 根据ID查询供应商
     */
    Supplier selectSupplierById(Long id);
    
    /**
     * 新增供应商
     */
    int insertSupplier(Supplier supplier);
    
    /**
     * 修改供应商
     */
    int updateSupplier(Supplier supplier);
    
    /**
     * 删除供应商
     */
    int deleteSupplierById(Long id);
}
