package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Payment;
import com.enterprisehub.system.mapper.PaymentMapper;
import com.enterprisehub.system.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    
    @Override
    public PageResult<Payment> selectPage(PageQuery pageQuery) {
        Page<Payment> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Payment> result = this.page(page);
        return PageResult.build(result);
    }
    
    @Override
    public Payment selectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean insert(Payment entity) {
        return this.save(entity);
    }
    
    @Override
    public boolean update(Payment entity) {
        return this.updateById(entity);
    }
    
    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
