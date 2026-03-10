package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.Product;
import com.enterprisehub.system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/v1/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService service;
    
    @GetMapping
    public R<PageResult<Product>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }
    
    @GetMapping("/{id}")
    public R<Product> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }
    
    @PostMapping
    public R<Void> add(@RequestBody Product entity) {
        service.insert(entity);
        return R.ok();
    }
    
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Product entity) {
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
