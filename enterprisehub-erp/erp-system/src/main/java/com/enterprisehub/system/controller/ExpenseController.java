package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.Expense;
import com.enterprisehub.system.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    
    private final ExpenseService service;
    
    @GetMapping
    public R<PageResult<Expense>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }
    
    @GetMapping("/{id}")
    public R<Expense> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }
    
    @PostMapping
    public R<Void> add(@RequestBody Expense entity) {
        service.insert(entity);
        return R.ok();
    }
    
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Expense entity) {
        entity.setId(id);
        service.update(entity);
        return R.ok();
    }
    
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return R.ok();
    }
}
