package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.Customer;
import com.enterprisehub.system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public R<PageResult<Customer>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }

    @GetMapping("/{id}")
    public R<Customer> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody Customer entity) {
        service.insert(entity);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Customer entity) {
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
