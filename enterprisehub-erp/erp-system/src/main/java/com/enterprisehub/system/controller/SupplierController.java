package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.Supplier;
import com.enterprisehub.system.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商管理控制器
 */
@RestController
@RequestMapping("/purchase/v1/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 分页查询供应商列表
     */
    @GetMapping
    public R<PageResult<Supplier>> list(PageQuery pageQuery, String supplierName) {
        PageResult<Supplier> pageResult = supplierService.selectSupplierList(pageQuery, supplierName);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询供应商详情
     */
    @GetMapping("/{id}")
    public R<Supplier> getInfo(@PathVariable Long id) {
        Supplier supplier = supplierService.selectSupplierById(id);
        return R.ok(supplier);
    }

    /**
     * 新增供应商
     */
    @PostMapping
    @OperationLog(title = "供应商管理", businessType = 1)
    public R<Void> add(@RequestBody Supplier supplier) {
        supplierService.insertSupplier(supplier);
        return R.ok();
    }

    /**
     * 修改供应商
     */
    @PutMapping("/{id}")
    @OperationLog(module = "供应商管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        supplierService.updateSupplier(supplier);
        return R.ok();
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "供应商管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        supplierService.deleteSupplierById(id);
        return R.ok();
    }
}
