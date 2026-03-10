package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.StockCheck;
import com.enterprisehub.system.service.StockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存盘点管理控制器
 */
@RestController
@RequestMapping("/inventory/v1/stock-checks")
public class StockCheckController {

    @Autowired
    private StockCheckService stockCheckService;

    /**
     * 分页查询盘点单列表
     */
    @GetMapping
    public R<PageResult<StockCheck>> list(PageQuery pageQuery) {
        PageResult<StockCheck> pageResult = stockCheckService.selectPage(pageQuery);
        return R.ok(pageResult);
    }

    /**
     * 根据ID查询盘点单详情
     */
    @GetMapping("/{id}")
    public R<StockCheck> getInfo(@PathVariable Long id) {
        StockCheck stockCheck = stockCheckService.selectById(id);
        return R.ok(stockCheck);
    }

    /**
     * 新增盘点单
     */
    @PostMapping
    @OperationLog(title = "库存盘点", businessType = 1)
    public R<Void> add(@RequestBody StockCheck stockCheck) {
        stockCheckService.insert(stockCheck);
        return R.ok();
    }

    /**
     * 修改盘点单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "库存盘点", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody StockCheck stockCheck) {
        stockCheck.setId(id);
        stockCheckService.update(stockCheck);
        return R.ok();
    }

    /**
     * 删除盘点单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "库存盘点", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        stockCheckService.deleteById(id);
        return R.ok();
    }

    /**
     * 审核盘点单
     */
    @PutMapping("/{id}/audit")
    @OperationLog(module = "库存盘点", businessType = 1)
    public R<Void> audit(@PathVariable Long id) {
        stockCheckService.auditStockCheck(id);
        return R.ok();
    }
}
