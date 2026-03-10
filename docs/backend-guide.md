# EnterpriseHub ERP 后端开发规范

## 1. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 语言版本 |
| Spring Boot | 3.2.x | 核心框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| Spring Security + JWT | - | 认证授权 |
| MySQL | 8.0+ | 数据库 |
| Redis | 7.0+ | 缓存 |
| Lombok | 1.18+ | 简化代码 |
| MapStruct | 1.5+ | 对象映射 |

---

## 2. 项目结构

```
erp-system/src/main/java/com/enterprisehub/system/
├── SystemApplication.java        # 启动类
├── config/                       # 配置类
│   └── SecurityConfig.java
├── security/                     # 安全
│   └── JwtAuthenticationFilter.java
├── controller/                   # 控制器
├── service/                      # 服务接口
│   └── impl/                     # 服务实现
├── mapper/                       # Mapper接口
└── domain/
    ├── entity/                   # 数据库实体
    ├── dto/                      # 请求参数DTO
    └── vo/                       # 响应数据VO
```

---

## 3. 编码规范

### 3.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 包名 | 全小写 | `com.enterprisehub.system.controller` |
| 类名 | 大驼峰 | `SysUserController` |
| 方法名 | 小驼峰 | `getUserById` |
| 常量 | 全大写+下划线 | `MAX_PAGE_SIZE` |
| 数据库字段 | 小写+下划线 | `create_time` |
| 实体字段 | 小驼峰 | `createTime` |

### 3.2 类命名约定

| 后缀 | 说明 | 示例 |
|------|------|------|
| Controller | 控制器 | `SysUserController` |
| Service | 服务接口 | `SysUserService` |
| ServiceImpl | 服务实现 | `SysUserServiceImpl` |
| Mapper | 数据访问 | `SysUserMapper` |
| Entity/无后缀 | 数据库实体 | `SysUser` |
| DTO | 请求参数 | `UserCreateDTO` |
| VO | 响应数据 | `UserVO` |

### 3.3 Controller 规范

```java
@RestController
@RequestMapping("/system/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @GetMapping
    public R<PageResult<SysUser>> list(PageQuery query) {
        return R.ok(userService.selectPage(query));
    }

    @GetMapping("/{id}")
    public R<SysUser> getById(@PathVariable Long id) {
        return R.ok(userService.getById(id));
    }

    @PostMapping
    public R<Void> create(@RequestBody @Valid UserCreateDTO dto) {
        userService.createUser(dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
        userService.updateUser(id, dto);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok();
    }
}
```

**规则**：
- Controller 只做参数接收和响应返回，不写业务逻辑
- 使用 `@Valid` 进行参数校验
- 统一使用 `R<T>` 返回

### 3.4 Service 规范

```java
public interface SysUserService {
    PageResult<SysUser> selectPage(PageQuery query);
    void createUser(UserCreateDTO dto);
}

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;

    @Override
    public PageResult<SysUser> selectPage(PageQuery query) {
        Page<SysUser> page = userMapper.selectPage(
            new Page<>(query.getPageNum(), query.getPageSize()),
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, 0)
                .orderByDesc(SysUser::getCreateTime)
        );
        return PageResult.of(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserCreateDTO dto) {
        // 校验用户名唯一
        if (userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }
        // 创建用户...
    }
}
```

**规则**：
- 业务逻辑写在 Service 层
- 写操作加 `@Transactional(rollbackFor = Exception.class)`
- 业务异常使用 `BusinessException`

### 3.5 Entity 规范

```java
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @JsonIgnore  // 不返回给前端
    private String password;

    private String nickname;
    private String realName;
    private String phone;
    private String email;
    private Long deptId;
    private Integer status;

    @TableLogic
    private Integer delFlag;
}
```

---

## 4. 统一规范

### 4.1 统一响应 R

```java
R.ok()                  // 成功，无数据
R.ok(data)              // 成功，有数据
R.fail("错误信息")       // 失败
R.fail(code, "错误信息") // 失败，自定义错误码
```

### 4.2 异常处理

- 业务异常：`throw new BusinessException("xxx")`
- 全局捕获：`GlobalExceptionHandler` 自动处理
- 不要在 Controller 中 try-catch

### 4.3 日志记录

```java
@OperationLog(module = "用户管理", operation = "新增用户")
@PostMapping
public R<Void> create(@RequestBody UserCreateDTO dto) {
    // ...
}
```

### 4.4 分页查询

- 请求参数继承 `PageQuery`（包含 pageNum、pageSize）
- 返回 `PageResult<T>`（包含 list、total、pageNum、pageSize）

---

## 5. Git 提交规范

```
feat: 新增用户管理模块
fix: 修复登录Token过期问题
docs: 更新API文档
refactor: 重构用户Service
style: 代码格式调整
test: 新增用户Service单元测试
chore: 升级Spring Boot版本
```
