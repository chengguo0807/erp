package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Product;

public interface ProductService {
    PageResult<Product> selectPage(PageQuery pageQuery);
    Product selectById(Long id);
    boolean insert(Product entity);
    boolean update(Product entity);
    boolean deleteById(Long id);
}
