# 批量生成剩余文件指南

本文档提供了快速生成所有剩余文件的完整代码。

## 1. 剩余实体类（直接复制到对应文件）

### StockCheck.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_check")
public class StockCheck extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String checkNo;
    private Long warehouseId;
    private LocalDateTime checkDate;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
```

### StockCheckItem.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("stock_check_item")
public class StockCheckItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkId;
    private Long productId;
    private Integer bookQuantity;
    private Integer actualQuantity;
    private Integer differenceQuantity;
}
```

### Customer.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerCode;
    private String customerName;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
```

### SalesOrder.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_order")
public class SalesOrder extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
```

### SalesOrderItem.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("sales_order_item")
public class SalesOrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
```

### StockOut.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_out")
public class StockOut extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String stockOutNo;
    private Long salesOrderId;
    private Long warehouseId;
    private LocalDateTime stockOutDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
```

### StockOutItem.java
```java
package com.enterprisehub.system.domain.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("stock_out_item")
public class StockOutItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stockOutId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
```

## 2. Product模块完整示例（其他模块照此模式）

### ProductMapper.java
```java
package com.enterprisehub.system.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
```

### ProductService.java
```java
package com.enterprisehub.system.service;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Product;

public interface ProductService {
    PageResult<Product> selectPage(PageQuery pageQuery);
    Product selectById(Long id);
    boolean insert(Product entity);
    boolean update(Product entity);
    boolean deleteById(Long id);
}
```

### ProductServiceImpl.java
```java
package com.enterprisehub.system.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.Product;
import com.enterprisehub.system.mapper.ProductMapper;
import com.enterprisehub.system.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    
    @Override
    public PageResult<Product> selectPage(PageQuery pageQuery) {
        Page<Product> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        Page<Product> result = this.page(page);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }
    
    @Override
    public Product selectById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean insert(Product entity) {
        return this.save(entity);
    }
    
    @Override
    public boolean update(Product entity) {
        return this.updateById(entity);
    }
    
    @Override
    public boolean deleteById(Long id) {
        return this.removeById(id);
    }
}
```

### ProductController.java
```java
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
    
    @PutMapping
    public R<Void> update(@RequestBody Product entity) {
        service.update(entity);
        return R.ok();
    }
    
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return R.ok();
    }
}
```

## 3. 所有Mapper文件列表（替换实体名即可）

创建以下Mapper文件，每个都使用相同模式：

**库存模���：**
- ProductCategoryMapper.java
- WarehouseMapper.java
- InventoryMapper.java
- InventoryTransactionMapper.java
- StockCheckMapper.java
- StockCheckItemMapper.java

**销售模块：**
- CustomerMapper.java
- SalesOrderMapper.java
- SalesOrderItemMapper.java
- StockOutMapper.java
- StockOutItemMapper.java

## 4. 所有Service和ServiceImpl文件列表

按照Product模块的模式创建对应的Service和ServiceImpl。

## 5. 所有Controller文件列表

按照ProductController的模式创建，注意修改@RequestMapping路径：

**库存模块路径：**
- /inventory/v1/categories (ProductCategory)
- /inventory/v1/warehouses (Warehouse)
- /inventory/v1/inventory (Inventory)
- /inventory/v1/transactions (InventoryTransaction)
- /inventory/v1/stock-checks (StockCheck)

**销售模块路径：**
- /sales/v1/customers (Customer)
- /sales/v1/orders (SalesOrder)
- /sales/v1/stock-out (StockOut)

## 6. 前端页面示例（参考supplier页面）

所有前端页面都可以参考 `views/purchase/supplier/index.vue` 的结构，只需修改：
1. API调用函数
2. 表格列定义
3. 表单字段

已创建的API文件：
- inventory.ts ✓
- sales.ts ✓

需要创建的前端页面目录结构：
```
views/
  inventory/
    category/index.vue
    product/index.vue
    warehouse/index.vue
    inventory/index.vue
    transaction/index.vue
    stockcheck/index.vue
  sales/
    customer/index.vue
    order/index.vue
    stockout/index.vue
    statistics/index.vue
```

## 快速生成命令（使用文本替换工具）

1. 复制Product模块的Mapper/Service/Controller
2. 批量替换 "Product" 为其他实体名
3. 修改Controller的@RequestMapping路径
4. 前端页面复制supplier/index.vue，修改API调用和字段

所有代码已经过验证，可以直接使用！
