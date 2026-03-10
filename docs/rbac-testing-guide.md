# RBAC功能测试指南

## 测试前准备

### 1. 确保后端服务正在运行
```bash
cd enterprisehub-erp/erp-system
mvn spring-boot:run
```

### 2. 确保前端服务正在运行
```bash
cd enterprisehub-erp-ui
npm run dev
```

### 3. 在数据库中创建测试角色和用户

#### 创建角色
```sql
-- 如果角色不存在，则创建
INSERT INTO sys_role (role_name, role_key, sort, status, create_time, update_time) VALUES
('超级管理员', 'admin', 1, 0, NOW(), NOW()),
('销售经理', 'sales_manager', 3, 0, NOW(), NOW()),
('仓库员工', 'warehouse_staff', 8, 0, NOW(), NOW()),
('财务员工', 'finance_staff', 10, 0, NOW(), NOW());
```

#### 创建测试用户
```sql
-- 创建销售经理测试账号
INSERT INTO sys_user (username, password, nickname, status, create_time, update_time) 
VALUES ('sales_mgr', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '销售经理', 0, NOW(), NOW());

-- 创建仓库员工测试账号
INSERT INTO sys_user (username, password, nickname, status, create_time, update_time) 
VALUES ('warehouse_staff', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '仓库员工', 0, NOW(), NOW());

-- 创建财务员工测试账号
INSERT INTO sys_user (username, password, nickname, status, create_time, update_time) 
VALUES ('finance_staff', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '财务员工', 0, NOW(), NOW());

-- 注意：上面的密码是 'admin123' 的BCrypt加密结果
```

#### 分配角色
```sql
-- 为admin用户分配admin角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.role_key = 'admin';

-- 为sales_mgr用户分配sales_manager角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'sales_mgr' AND r.role_key = 'sales_manager';

-- 为warehouse_staff用户分配warehouse_staff角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'warehouse_staff' AND r.role_key = 'warehouse_staff';

-- 为finance_staff用户分配finance_staff角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'finance_staff' AND r.role_key = 'finance_staff';
```

## 测试场景

### 测试1：管理员账号
**账号信息**：
- 用户名：admin
- 密码：admin123

**预期结果**：
- ✅ 可以看到所有菜单
- ✅ 可以访问系统管理模块
- ✅ 可以访问销售管理模块
- ✅ 可以访问采购管理模块
- ✅ 可以访问库存管理模块
- ✅ 可以访问财务管理模块
- ✅ 可以访问统计报表模块

### 测试2：销售经理账号
**账号信息**：
- 用户名：sales_mgr
- 密码：admin123

**预期结果**：
- ✅ 可以看到工作台
- ✅ 可以看到销售管理（销售订单、客户管理）
- ✅ 可以看到库存管理（仅库存查询）
- ✅ 可以看到统计报表（仅销售统计）
- ❌ 看不到系统管理模块
- ❌ 看不到采购管理模块
- ❌ 看不到财务管理模块

### 测试3：仓库员工账号
**账号信息**：
- 用户名：warehouse_staff
- 密码：admin123

**预期结果**：
- ✅ 可以看到工作台
- ✅ 可以看到采购管理（仅入库管理）
- ✅ 可以看到库存管理（产品管理、库存查询、出库管理、库存盘点、库存流水）
- ❌ 看不到系统管理模块
- ❌ 看不到销售管理模块
- ❌ 看不到财务管理模块
- ❌ 看不到统计报表模块

### 测试4：财务员工账号
**账号信息**：
- 用户名：finance_staff
- 密码：admin123

**预期结果**：
- ✅ 可以看到工作台
- ✅ 可以看到财务管理（收付款管理、费用管理）
- ❌ 看不到系统管理模块
- ❌ 看不到销售管理模块
- ❌ 看不到采购管理模块
- ❌ 看不到库存管理模块
- ❌ 看不到统计报表模块

### 测试5：权限控制测试
1. 使用sales_mgr账号登录
2. 在浏览器地址栏直接输入：`http://localhost:3000/system/user`
3. **预期结果**：应该跳转到403无权限页面

## 测试步骤

### 步骤1：登录测试
1. 打开浏览器访问：`http://localhost:3000/login`
2. 输入测试账号和密码
3. 点击登录按钮
4. 观察是否成功跳转到工作台

### 步骤2：菜单显示测试
1. 登录成功后，查看左侧菜单栏
2. 对照预期结果，检查菜单项是否正确显示
3. 打开浏览器控制台（F12），查看是否有错误信息
4. 在控制台中查看日志：
   - "用户角色: [...]" - 显示当前用户的角色
   - "过滤后的菜单: [...]" - 显示过滤后的菜单配置

### 步骤3：路由权限测试
1. 尝试点击有权限的菜单项，应该正常跳转
2. 在浏览器地址栏直接输入无权限的路由地址
3. 应该自动跳转到403页面

### 步骤4：退出登录测试
1. 点击右上角用户头像
2. 选择"退出登录"
3. 确认退出
4. 应该跳转回登录页面

## 调试技巧

### 1. 查看用户角色信息
打开浏览器控制台，输入：
```javascript
JSON.parse(localStorage.getItem('pinia'))
```
可以看到存储的用户信息和角色

### 2. 查看后端返回的角色数据
在登录时，打开浏览器Network标签，查看`/auth/login`接口的响应：
```json
{
  "code": 200,
  "data": {
    "token": "...",
    "userInfo": {
      "userId": 1,
      "username": "admin",
      "nickname": "管理员",
      "roles": ["admin"],
      "permissions": [...]
    }
  }
}
```

### 3. 检查路由守卫日志
如果访问被拒绝，控制台会输出：
```
用户无权限访问: /system/user 需要角色: ["admin", "system_admin"] 用户角色: ["sales_manager"]
```

## 常见问题

### 问题1：登录后看不到任何菜单
**原因**：用户没有分配角色，或者角色标识不匹配
**解决**：
1. 检查数据库`sys_user_role`表，确认用户已分配角色
2. 检查`sys_role`表中的`role_key`字段是否与前端配置一致

### 问题2：菜单显示不正确
**原因**：前端菜单配置的roles与数据库不一致
**解决**：
1. 检查`src/router/menus.ts`中的roles配置
2. 确保role_key与前端配置完全一致（区分大小写）

### 问题3：直接访问URL不会被拦截
**原因**：路由配置中没有设置meta.roles
**解决**：
1. 检查`src/router/index.ts`中的路由配置
2. 为需要权限控制的路由添加`meta: { roles: [...] }`

## 修改时间
2026-03-10
