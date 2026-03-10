@echo off
chcp 65001 >nul
echo ========================================
echo ERP系统代码批量生成脚本
echo ========================================
echo.

set BASE_PATH=enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system

:: 定义需要生成的实体列表
set ENTITIES=ProductCategory Warehouse Inventory InventoryTransaction StockCheck Customer SalesOrder StockOut

echo 开始生成Mapper文件...
for %%E in (%ENTITIES%) do (
    echo 生成 %%EMapper.java
    (
        echo package com.enterprisehub.system.mapper;
        echo.
        echo import com.baomidou.mybatisplus.core.mapper.BaseMapper;
        echo import com.enterprisehub.system.domain.entity.%%E;
        echo import org.apache.ibatis.annotations.Mapper;
        echo.
        echo @Mapper
        echo public interface %%EMapper extends BaseMapper^<%%E^> {
        echo }
    ) > "%BASE_PATH%\mapper\%%EMapper.java"
)

echo.
echo 开始生成Service接口文件...
for %%E in (%ENTITIES%) do (
    echo 生成 %%EService.java
    (
        echo package com.enterprisehub.system.service;
        echo.
        echo import com.enterprisehub.common.core.domain.PageQuery;
        echo import com.enterprisehub.common.core.domain.PageResult;
        echo import com.enterprisehub.system.domain.entity.%%E;
        echo.
        echo public interface %%EService {
        echo     PageResult^<%%E^> selectPage^(PageQuery pageQuery^);
        echo     %%E selectById^(Long id^);
        echo     boolean insert^(%%E entity^);
        echo     boolean update^(%%E entity^);
        echo     boolean deleteById^(Long id^);
        echo }
    ) > "%BASE_PATH%\service\%%EService.java"
)

echo.
echo 开始生成ServiceImpl文件...
for %%E in (%ENTITIES%) do (
    echo 生成 %%EServiceImpl.java
    (
        echo package com.enterprisehub.system.service.impl;
        echo.
        echo import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
        echo import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        echo import com.enterprisehub.common.core.domain.PageQuery;
        echo import com.enterprisehub.common.core.domain.PageResult;
        echo import com.enterprisehub.system.domain.entity.%%E;
        echo import com.enterprisehub.system.mapper.%%EMapper;
        echo import com.enterprisehub.system.service.%%EService;
        echo import org.springframework.stereotype.Service;
        echo.
        echo @Service
        echo public class %%EServiceImpl extends ServiceImpl^<%%EMapper, %%E^> implements %%EService {
        echo.
        echo     @Override
        echo     public PageResult^<%%E^> selectPage^(PageQuery pageQuery^) {
        echo         Page^<%%E^> page = new Page^<^>^(pageQuery.getPageNum^(^), pageQuery.getPageSize^(^)^);
        echo         Page^<%%E^> result = this.page^(page^);
        echo         return new PageResult^<^>^(result.getRecords^(^), result.getTotal^(^)^);
        echo     }
        echo.
        echo     @Override
        echo     public %%E selectById^(Long id^) {
        echo         return this.getById^(id^);
        echo     }
        echo.
        echo     @Override
        echo     public boolean insert^(%%E entity^) {
        echo         return this.save^(entity^);
        echo     }
        echo.
        echo     @Override
        echo     public boolean update^(%%E entity^) {
        echo         return this.updateById^(entity^);
        echo     }
        echo.
        echo     @Override
        echo     public boolean deleteById^(Long id^) {
        echo         return this.removeById^(id^);
        echo     }
        echo }
    ) > "%BASE_PATH%\service\impl\%%EServiceImpl.java"
)

echo.
echo 开始生成Controller文件...

:: ProductCategory
echo 生成 ProductCategoryController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.ProductCategory;
    echo import com.enterprisehub.system.service.ProductCategoryService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/inventory/v1/categories"^)
    echo @RequiredArgsConstructor
    echo public class ProductCategoryController {
    echo     private final ProductCategoryService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<ProductCategory^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<ProductCategory^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody ProductCategory entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody ProductCategory entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
        echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\ProductCategoryController.java"

:: Warehouse
echo 生成 WarehouseController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.Warehouse;
    echo import com.enterprisehub.system.service.WarehouseService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/inventory/v1/warehouses"^)
    echo @RequiredArgsConstructor
    echo public class WarehouseController {
    echo     private final WarehouseService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<Warehouse^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<Warehouse^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody Warehouse entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody Warehouse entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\WarehouseController.java"

:: Inventory
echo 生成 InventoryController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.Inventory;
    echo import com.enterprisehub.system.service.InventoryService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/inventory/v1/inventory"^)
    echo @RequiredArgsConstructor
    echo public class InventoryController {
    echo     private final InventoryService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<Inventory^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<Inventory^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody Inventory entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody Inventory entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\InventoryController.java"

:: InventoryTransaction
echo 生成 InventoryTransactionController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.InventoryTransaction;
    echo import com.enterprisehub.system.service.InventoryTransactionService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/inventory/v1/transactions"^)
    echo @RequiredArgsConstructor
    echo public class InventoryTransactionController {
    echo     private final InventoryTransactionService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<InventoryTransaction^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<InventoryTransaction^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody InventoryTransaction entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody InventoryTransaction entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\InventoryTransactionController.java"

:: StockCheck
echo 生成 StockCheckController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.StockCheck;
    echo import com.enterprisehub.system.service.StockCheckService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/inventory/v1/stock-checks"^)
    echo @RequiredArgsConstructor
    echo public class StockCheckController {
    echo     private final StockCheckService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<StockCheck^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<StockCheck^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody StockCheck entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody StockCheck entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\StockCheckController.java"

:: Customer
echo 生成 CustomerController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.Customer;
    echo import com.enterprisehub.system.service.CustomerService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/sales/v1/customers"^)
    echo @RequiredArgsConstructor
    echo public class CustomerController {
    echo     private final CustomerService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<Customer^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<Customer^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody Customer entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody Customer entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\CustomerController.java"

:: SalesOrder
echo 生成 SalesOrderController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.SalesOrder;
    echo import com.enterprisehub.system.service.SalesOrderService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/sales/v1/orders"^)
    echo @RequiredArgsConstructor
    echo public class SalesOrderController {
    echo     private final SalesOrderService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<SalesOrder^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<SalesOrder^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody SalesOrder entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody SalesOrder entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\SalesOrderController.java"

:: StockOut
echo 生成 StockOutController.java
(
    echo package com.enterprisehub.system.controller;
    echo.
    echo import com.enterprisehub.common.core.domain.PageQuery;
    echo import com.enterprisehub.common.core.domain.PageResult;
    echo import com.enterprisehub.common.core.domain.R;
    echo import com.enterprisehub.system.domain.entity.StockOut;
    echo import com.enterprisehub.system.service.StockOutService;
    echo import lombok.RequiredArgsConstructor;
    echo import org.springframework.web.bind.annotation.*;
    echo.
    echo @RestController
    echo @RequestMapping^("/sales/v1/stock-out"^)
    echo @RequiredArgsConstructor
    echo public class StockOutController {
    echo     private final StockOutService service;
    echo.
    echo     @GetMapping
    echo     public R^<PageResult^<StockOut^>^> list^(PageQuery pageQuery^) {
    echo         return R.ok^(service.selectPage^(pageQuery^)^);
    echo     }
    echo.
    echo     @GetMapping^("/{id}"^)
    echo     public R^<StockOut^> getById^(@PathVariable Long id^) {
    echo         return R.ok^(service.selectById^(id^)^);
    echo     }
    echo.
    echo     @PostMapping
    echo     public R^<Void^> add^(@RequestBody StockOut entity^) {
    echo         service.insert^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @PutMapping
    echo     public R^<Void^> update^(@RequestBody StockOut entity^) {
    echo         service.update^(entity^);
    echo         return R.ok^(^);
    echo     }
    echo.
    echo     @DeleteMapping^("/{id}"^)
    echo     public R^<Void^> delete^(@PathVariable Long id^) {
    echo         service.deleteById^(id^);
    echo         return R.ok^(^);
    echo     }
    echo }
) > "%BASE_PATH%\controller\StockOutController.java"

echo.
echo ========================================
echo 代码生成完成！
echo ========================================
echo 已生成：
echo - 8个Mapper文件
echo - 8个Service接口文件
echo - 8个ServiceImpl实现文件
echo - 8个Controller文件
echo.
echo 共计32个文件
echo ========================================
pause
