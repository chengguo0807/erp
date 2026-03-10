package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Expense;
import com.enterprisehub.system.mapper.ExpenseMapper;
import com.enterprisehub.system.service.ExpenseService;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense> implements ExpenseService {
    
    @Override
    public PageResult<Expense> selectPage(PageQuery pageQuery) {
        Page<Expense> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Expense> result = this.page(page);
        return PageResult.build(result);
    }
    
    @Override
    public Expense selectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean insert(Expense entity) {
        return this.save(entity);
    }
    
    @Override
    public boolean update(Expense entity) {
        return this.updateById(entity);
    }
    
    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
