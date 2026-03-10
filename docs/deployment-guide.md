# EnterpriseHub ERP 部署运维手册

## 1. 环境要求

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | Java运行环境 |
| Maven | 3.8+ | 构建工具 |
| Node.js | 18+ | 前端构建 |
| MySQL | 8.0+ | 数据库 |
| Redis | 7.0+ | 缓存 |
| Docker | 20+ | 容器化部署（可选） |

---

## 2. 开发环境部署

### 2.1 数据库初始化

```bash
# 创建数据库并导入初始化脚本
mysql -u root -p < enterprisehub-erp/sql/init.sql
```

### 2.2 后端启动

```bash
cd enterprisehub-erp

# 编译打包
mvn clean install -DskipTests

# 启动系统管理服务
cd erp-system
mvn spring-boot:run

# 或直接运行jar
java -jar target/erp-system.jar
```

后端服务默认端口：
- erp-gateway: 8080
- erp-system: 8082

### 2.3 前端启动

```bash
cd enterprisehub-erp-ui

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端开发服务器默认端口：3000，自动代理 `/api` 到后端 `http://localhost:8080`

### 2.4 修改配置

后端配置文件：`erp-system/src/main/resources/application.yml`

```yaml
# 数据库连接
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_system
    username: root
    password: root123

# Redis连接
  redis:
    host: localhost
    port: 6379

# JWT密钥
jwt:
  secret: your-secret-key
  expiration: 7200  # 2小时
```

---

## 3. Docker Compose 部署

### 3.1 一键启动

```bash
cd enterprisehub-erp
docker-compose up -d
```

### 3.2 服务说明

| 服务 | 端口 | 说明 |
|------|------|------|
| mysql | 3306 | MySQL数据库 |
| redis | 6379 | Redis缓存 |
| erp-system | 8082 | 后端服务 |
| erp-gateway | 8080 | API网关 |

### 3.3 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f erp-system
```

### 3.4 停止服务

```bash
docker-compose down
```

---

## 4. 生产环境部署

### 4.1 前端构建

```bash
cd enterprisehub-erp-ui
npm run build
```

构建产物在 `dist/` 目录，部署到 Nginx：

```nginx
server {
    listen 80;
    server_name erp.example.com;

    root /usr/share/nginx/html;
    index index.html;

    # 前端路由
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 4.2 后端部署

```bash
# 打包
cd enterprisehub-erp
mvn clean package -DskipTests

# 启动（后台运行）
nohup java -jar erp-system/target/erp-system.jar \
  --spring.profiles.active=prod \
  > /var/log/erp-system.log 2>&1 &
```

### 4.3 生产环境配置建议

```yaml
spring:
  datasource:
    url: jdbc:mysql://db-host:3306/erp_system?useSSL=true
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

  redis:
    host: ${REDIS_HOST}
    password: ${REDIS_PASSWORD}

server:
  port: 8082

logging:
  level:
    root: WARN
    com.enterprisehub: INFO
  file:
    name: /var/log/erp-system.log
```

---

## 5. 数据备份

### 5.1 MySQL 备份

```bash
# 全量备份
mysqldump -u root -p erp_system > backup_$(date +%Y%m%d).sql

# 恢复
mysql -u root -p erp_system < backup_20260309.sql
```

### 5.2 定时备份（crontab）

```bash
# 每天凌晨2点自动备份
0 2 * * * mysqldump -u root -pYourPassword erp_system > /backup/erp_$(date +\%Y\%m\%d).sql
```

---

## 6. 常见问题

### Q1: 端口被占用
```bash
# 查看端口占用
netstat -tlnp | grep 8082
# 杀掉进程
kill -9 <PID>
```

### Q2: MySQL 连接失败
- 检查 MySQL 是否启动
- 检查用户名密码是否正确
- 检查防火墙是否放行3306端口

### Q3: Redis 连接失败
- 检查 Redis 是否启动：`redis-cli ping`
- 检查 Redis 密码配置

### Q4: 前端页面空白
- 检查后端服务是否启动
- 检查浏览器控制台是否有接口报错
- 检查 Nginx 代理配置
