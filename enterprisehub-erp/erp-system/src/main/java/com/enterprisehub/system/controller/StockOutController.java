package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.StockOut;
import com.enterprisehub.system.service.StockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 出库单管理控制器
 */
@RestController
@RequestMapping("/inventory/v1/stock-outs")
public class StockOutController {

    @Autowired
    private StockOutService stockOutService;

    /**
     * 分页查询出库单列表
     */
    @GetMapping
    public R<PageResult<StockOut>> list(PageQuery pageQuery) {
        PageResult<StockOut> pageResult = stockOutService.selectPage(pageQuery);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询出库单详情
     */
    @GetMapping("/{id}")
    public R<StockOut> getInfo(@PathVariable Long id) {
        StockOut stockOut = stockOutService.selectById(id);
        return R.ok(stockOut);
    }

    /**
     * 新增出库单
     */
    @PostMapping
    @OperationLog(title = "出库单", businessType = 1)
    public R<Void> add(@RequestBody StockOut stockOut) {
        stockOutService.insert(stockOut);
        return R.ok();
    }

    /**
     * 修改出库单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "出库单", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody StockOut stockOut) {
        stockOut.setId(id);
        stockOutService.update(stockOut);
        return R.ok();
    }

    /**
     * 删除出库单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "出库单", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        stockOutService.deleteById(id);
        return R.ok();
    }

    /**
     * 审核出库单
     */
    @PutMapping("/{id}/audit")
    @OperationLog(module = "出库单", businessType = 1)
    public R<Void> audit(@PathVariable Long id) {
        stockOutService.auditStockOut(id);
        return R.ok();
    }
}
