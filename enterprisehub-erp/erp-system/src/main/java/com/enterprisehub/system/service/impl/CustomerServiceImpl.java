package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Customer;
import com.enterprisehub.system.mapper.CustomerMapper;
import com.enterprisehub.system.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public PageResult<Customer> selectPage(PageQuery pageQuery) {
        Page<Customer> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Customer> result = this.page(page);
        return PageResult.build(result);
    }

    @Override
    public Customer selectById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean insert(Customer entity) {
        return this.save(entity);
    }

    @Override
    public boolean update(Customer entity) {
        return this.updateById(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
