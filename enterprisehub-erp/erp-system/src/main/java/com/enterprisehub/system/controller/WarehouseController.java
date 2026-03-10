package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.Warehouse;
import com.enterprisehub.system.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService service;

    @GetMapping
    public R<PageResult<Warehouse>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }

    @GetMapping("/{id}")
    public R<Warehouse> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody Warehouse entity) {
        service.insert(entity);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Warehouse entity) {
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
