package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.StockIn;
import com.enterprisehub.system.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 入库管理控制器
 */
@RestController
@RequestMapping("/purchase/v1/stockin")
public class StockInController {

    @Autowired
    private StockInService stockInService;

    /**
     * 分页查询入库单列表
     */
    @GetMapping
    public R<PageResult<StockIn>> list(PageQuery pageQuery, String stockInNo, Integer status) {
        PageResult<StockIn> pageResult = stockInService.selectStockInList(pageQuery, stockInNo, status);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询入库单详情
     */
    @GetMapping("/{id}")
    public R<StockIn> getInfo(@PathVariable Long id) {
        StockIn stockIn = stockInService.selectStockInById(id);
        return R.ok(stockIn);
    }

    /**
     * 新增入库单
     */
    @PostMapping
    @OperationLog(title = "入库管理", businessType = 1)
    public R<Void> add(@RequestBody StockIn stockIn) {
        stockInService.insertStockIn(stockIn);
        return R.ok();
    }

    /**
     * 修改入库单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "入库管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody StockIn stockIn) {
        stockIn.setId(id);
        stockInService.updateStockIn(stockIn);
        return R.ok();
    }

    /**
     * 删除入库单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "入库管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        stockInService.deleteStockInById(id);
        return R.ok();
    }

    /**
     * 审核入库单
     */
    @PutMapping("/{id}/audit")
    @OperationLog(module = "入库管理", businessType = 1)
    public R<Void> audit(@PathVariable Long id) {
        stockInService.auditStockIn(id);
        return R.ok();
    }
}
