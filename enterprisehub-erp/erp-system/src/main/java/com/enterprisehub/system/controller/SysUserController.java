package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.system.domain.dto.SysUserDTO;
import com.enterprisehub.system.domain.vo.SysUserVO;
import com.enterprisehub.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @Operation(summary = "用户列表")
    @GetMapping
    public R<PageResult<SysUserVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long deptId) {
        return R.ok(sysUserService.getUserPage(pageNum, pageSize, username, status, deptId));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public R<SysUserVO> getById(@PathVariable Long id) {
        // TODO: 实现获取单个用户详情
        return R.ok();
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<Void> create(@RequestBody SysUserDTO userDTO) {
        sysUserService.createUser(userDTO);
        return R.ok();
    }

    @Operation(summary = "修改用户")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody SysUserDTO userDTO) {
        userDTO.setId(id);
        sysUserService.updateUser(userDTO);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    public R<Void> resetPassword(@PathVariable Long id, @RequestBody String newPassword) {
        sysUserService.resetPassword(id, newPassword);
        return R.ok();
    }
}
