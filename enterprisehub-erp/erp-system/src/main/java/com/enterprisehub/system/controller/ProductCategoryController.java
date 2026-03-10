package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.ProductCategory;
import com.enterprisehub.system.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/v1/categories")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService service;

    @GetMapping
    public R<PageResult<ProductCategory>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }

    @GetMapping("/{id}")
    public R<ProductCategory> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody ProductCategory entity) {
        service.insert(entity);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody ProductCategory entity) {
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
