package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Warehouse;

public interface WarehouseService {
    PageResult<Warehouse> selectPage(PageQuery pageQuery);
    Warehouse selectById(Long id);
    boolean insert(Warehouse entity);
    boolean update(Warehouse entity);
    boolean deleteById(Long id);
}
