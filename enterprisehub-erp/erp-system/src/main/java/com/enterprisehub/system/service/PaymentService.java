package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Payment;

public interface PaymentService {
    PageResult<Payment> selectPage(PageQuery pageQuery);
    Payment selectById(Long id);
    boolean insert(Payment entity);
    boolean update(Payment entity);
    boolean deleteById(Long id);
}
