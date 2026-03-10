package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.Payment;
import com.enterprisehub.system.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService service;
    
    @GetMapping
    public R<PageResult<Payment>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }
    
    @GetMapping("/{id}")
    public R<Payment> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }
    
    @PostMapping
    public R<Void> add(@RequestBody Payment entity) {
        service.insert(entity);
        return R.ok();
    }
    
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Payment entity) {
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
