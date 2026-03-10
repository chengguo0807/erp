package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.SysDept;
import com.enterprisehub.system.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/system/v1/depts")
public class SysDeptController {

    @Autowired
    private SysDeptService deptService;

    /**
     * 查询部门列表（树形结构）
     */
    @GetMapping
    public R<List<SysDept>> list(String deptName) {
        List<SysDept> list = deptService.selectDeptList(deptName);
        return R.ok(list);
    }

    /**
     * 根据ID查询部门详情
     */
    @GetMapping("/{id}")
    public R<SysDept> getInfo(@PathVariable Long id) {
        SysDept dept = deptService.selectDeptById(id);
        return R.ok(dept);
    }

    /**
     * 新增部门
     */
    @PostMapping
    @OperationLog(title = "部门管理", businessType = 1)
    public R<Void> add(@RequestBody SysDept dept) {
        deptService.insertDept(dept);
        return R.ok();
    }

    /**
     * 修改部门
     */
    @PutMapping("/{id}")
    @OperationLog(module = "部门管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody SysDept dept) {
        dept.setId(id);
        deptService.updateDept(dept);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "部门管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        deptService.deleteDeptById(id);
        return R.ok();
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/tree")
    public R<List<SysDept>> getDeptTree() {
        List<SysDept> deptTree = deptService.selectDeptTree();
        return R.ok(deptTree);
    }
}
