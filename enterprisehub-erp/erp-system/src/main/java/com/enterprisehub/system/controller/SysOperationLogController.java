package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.entity.SysOperationLog;
import com.enterprisehub.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/v1/operation-logs")
@RequiredArgsConstructor
public class SysOperationLogController {
    
    private final SysOperationLogService service;
    
    @GetMapping
    public R<PageResult<SysOperationLog>> list(PageQuery pageQuery) {
        return R.ok(service.selectPage(pageQuery));
    }
    
    @GetMapping("/{id}")
    public R<SysOperationLog> getById(@PathVariable Long id) {
        return R.ok(service.selectById(id));
    }
    
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return R.ok();
    }
}
