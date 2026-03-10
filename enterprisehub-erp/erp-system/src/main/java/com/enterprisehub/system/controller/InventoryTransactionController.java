package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.InventoryTransaction;
import com.enterprisehub.system.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory/v1/transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {
    private final InventoryTransactionService service;

    @GetMapping
    public R<PageResult<InventoryTransaction>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        return R.ok(service.selectPage(pageQuery, productId, warehouseId));
    }

    @GetMapping("/{id}")
    public R<InventoryTransaction> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody InventoryTransaction entity) {
        service.insert(entity);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody InventoryTransaction entity) {
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
