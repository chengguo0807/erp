package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.dto.InventoryTransferDTO;
import com.enterprisehub.system.domain.entity.Inventory;
import com.enterprisehub.system.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存管理控制器
 */
@RestController
@RequestMapping("/inventory/v1/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 分页查询库存列表
     */
    @GetMapping
    public R<PageResult<Inventory>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId) {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        PageResult<Inventory> pageResult = inventoryService.selectPage(pageQuery, productId, warehouseId);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询库存详情
     */
    @GetMapping("/{id}")
    public R<Inventory> getInfo(@PathVariable Long id) {
        Inventory inventory = inventoryService.selectById(id);
        return R.ok(inventory);
    }

    /**
     * 新增库存
     */
    @PostMapping
    @OperationLog(title = "库存管理", businessType = 1)
    public R<Void> add(@RequestBody Inventory inventory) {
        inventoryService.insert(inventory);
        return R.ok();
    }

    /**
     * 修改库存
     */
    @PutMapping("/{id}")
    @OperationLog(module = "库存管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        inventoryService.update(inventory);
        return R.ok();
    }

    /**
     * 删除库存
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "库存管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        inventoryService.deleteById(id);
        return R.ok();
    }

    /**
     * 获取库存预警列表
     */
    @GetMapping("/warning")
    public R<List<Inventory>> getWarningList() {
        List<Inventory> list = inventoryService.getWarningList();
        return R.ok(list);
    }

    /**
     * 库存调拨
     */
    @PostMapping("/transfer")
    @OperationLog(title = "库存管理", businessType = 2)
    public R<Void> transfer(@RequestBody InventoryTransferDTO transferDTO) {
        inventoryService.transfer(transferDTO);
        return R.ok();
    }
}
