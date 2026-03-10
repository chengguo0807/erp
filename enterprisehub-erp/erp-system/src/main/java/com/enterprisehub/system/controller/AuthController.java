package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.constant.Constants;
import com.enterprisehub.common.core.domain.LoginUser;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.core.exception.BusinessException;
import com.enterprisehub.common.security.utils.JwtUtils;
import com.enterprisehub.common.security.utils.SecurityUtils;
import com.enterprisehub.system.domain.entity.SysUser;
import com.enterprisehub.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证控制器（登录/登出）
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService sysUserService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // 1. 查询用户
        SysUser user = sysUserService.getByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 校验状态
        if (user.getStatus() != 0) {
            throw new BusinessException("该账号已被停用");
        }

        // 3. 校验密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 4. 构建登录用户信息
        LoginUser loginUser = sysUserService.buildLoginUser(user);

        // 5. 生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("tenantId", user.getTenantId());
        String token = jwtUtils.generateToken(user.getUsername(), claims);
        loginUser.setToken(token);

        // 6. 存入Redis
        redisTemplate.opsForValue().set(
                Constants.LOGIN_USER_KEY + user.getUsername(),
                loginUser, 24, TimeUnit.HOURS);

        // 7. 返回Token
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", loginUser);

        return R.ok(result);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public R<Void> logout() {
        String username = SecurityUtils.getCurrentUsername();
        if (username != null) {
            redisTemplate.delete(Constants.LOGIN_USER_KEY + username);
        }
        return R.ok();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public R<LoginUser> getUserInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(401, "未登录或登录已过期");
        }
        return R.ok(loginUser);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
