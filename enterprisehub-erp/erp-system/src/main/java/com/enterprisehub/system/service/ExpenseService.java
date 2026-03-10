package com.enterprisehub.system.service;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Expense;

public interface ExpenseService {
    PageResult<Expense> selectPage(PageQuery pageQuery);
    Expense selectById(Long id);
    boolean insert(Expense entity);
    boolean update(Expense entity);
    boolean deleteById(Long id);
}
