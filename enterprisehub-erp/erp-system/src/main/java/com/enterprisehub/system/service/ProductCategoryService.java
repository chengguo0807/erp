package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.ProductCategory;

public interface ProductCategoryService {
    PageResult<ProductCategory> selectPage(PageQuery pageQuery);
    ProductCategory selectById(Long id);
    boolean insert(ProductCategory entity);
    boolean update(ProductCategory entity);
    boolean deleteById(Long id);
}
