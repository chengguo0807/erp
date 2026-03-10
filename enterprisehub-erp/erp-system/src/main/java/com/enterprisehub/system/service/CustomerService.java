package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Customer;

public interface CustomerService {
    PageResult<Customer> selectPage(PageQuery pageQuery);
    Customer selectById(Long id);
    boolean insert(Customer entity);
    boolean update(Customer entity);
    boolean deleteById(Long id);
}
