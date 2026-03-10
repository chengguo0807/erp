# 角色管理500错误修复记录

## 问题描述
访问角色管理页面时，前端报500错误：
```
GET http://localhost:3000/api/system/v1/roles?pageNum=1&pageSize=10&roleName= 500 (Internal Server Error)
```

## 错误原因
后端日志显示错误：
```
Caused by: org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): 
com.enterprisehub.system.mapper.SysRoleMenuMapper.selectMenuIdsByRoleId
```

问题根源：`SysRoleMenuMapper`接口中定义了`selectMenuIdsByRoleId`方法，但缺少对应的MyBatis XML映射文件。

## 修复步骤

### 1. 创建MyBatis XML映射文件
创建文件：`enterprisehub-erp/erp-system/src/main/resources/mapper/SysRoleMenuMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.enterprisehub.system.mapper.SysRoleMenuMapper">

    <!-- 根据角色ID查询菜单ID列表 -->
    <select id="selectMenuIdsByRoleId" resultType="java.lang.Long">
        SELECT menu_id
        FROM sys_role_menu
        WHERE role_id = #{roleId}
    </select>

</mapper>
```

### 2. 重新编译项目
```bash
cd enterprisehub-erp
mvn clean install -DskipTests
```

### 3. 重启后端服务
```bash
cd erp-system
mvn spring-boot:run
```

## 验证结果
- ✅ 后端服务启动成功
- ✅ 角色列表查询成功（返回3条记录）
- ✅ 部门列表查询成功（返回9条记录）
- ✅ 前端页面可以正常访问

## 相关文件
- `enterprisehub-erp/erp-system/src/main/resources/mapper/SysRoleMenuMapper.xml` - 新增
- `enterprisehub-erp/erp-system/src/main/java/com/enterprisehub/system/mapper/SysRoleMenuMapper.java` - 已存在
- `enterprisehub-erp/erp-system/src/main/resources/application.yml` - MyBatis配置

## 注意事项
- MyBatis的mapper XML文件路径配置在`application.yml`中：`mybatis-plus.mapper-locations: classpath:mapper/**/*.xml`
- 所有自定义SQL查询都需要在XML文件中定义
- MyBatis-Plus提供的基础CRUD方法不需要XML映射

## 修复时间
2026-03-10 09:41
