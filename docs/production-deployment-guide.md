# EnterpriseHub ERP 生产环境部署完整指南

> 本指南将手把手教你如何将ERP系统部署到生产服务器，让用户可以通过域名访问你的系统。

---

## 📋 部署前准备清单

在开始部署之前，请确保你已经准备好以下内容：

### ✅ 必需项
- [ ] **域名**：你已经有了（例如：erp.yourdomain.com）
- [ ] **云服务器**：阿里云/腾讯云/华为云等（推荐配置见下方）
- [ ] **SSL证书**：用于HTTPS加密（可免费申请）
- [ ] **备案**：如果服务器在中国大陆，域名需要完成ICP备案

### 💻 推荐服务器配置

| 用户规模 | CPU | 内存 | 硬盘 | 带宽 | 月费用（参考） |
|---------|-----|------|------|------|---------------|
| 小型（<50人） | 2核 | 4GB | 40GB | 3Mbps | ¥200-300 |
| 中型（50-200人） | 4核 | 8GB | 80GB | 5Mbps | ¥500-800 |
| 大型（>200人） | 8核 | 16GB | 200GB | 10Mbps | ¥1500+ |

### 🔧 服务器操作系统
- **推荐**：Ubuntu 22.04 LTS 或 CentOS 7/8
- **不推荐**：Windows Server（配置复杂，成本高）

---

## 第一步：购买和配置云服务器

### 1.1 购买云服务器

以阿里云为例（其他云平台类似）：

1. 登录 [阿里云控制台](https://www.aliyun.com)
2. 选择 **云服务器ECS** → **立即购买**
3. 选择配置：
   - **地域**：选择离你用户最近的地区
   - **实例规格**：根据上表选择
   - **镜像**：Ubuntu 22.04 64位
   - **存储**：系统盘40GB起
   - **网络**：按使用流量或固定带宽
4. 设置**安全组**（重要！）：
   - 放行端口：22（SSH）、80（HTTP）、443（HTTPS）
   - 暂时放行：3306（MySQL）、6379（Redis）、8080（后端）用于调试
5. 设置**root密码**（务必记住）
6. 完成购买

### 1.2 域名解析

1. 登录你的域名服务商（如阿里云、腾讯云、GoDaddy）
2. 进入**域名管理** → **DNS解析设置**
3. 添加A记录：
   ```
   记录类型：A
   主机记录：erp（或 @代表根域名）
   记录值：你的服务器公网IP（如：123.45.67.89）
   TTL：10分钟
   ```
4. 等待5-10分钟生效，用 `ping erp.yourdomain.com` 测试

### 1.3 申请SSL证书（免费）

**方式一：云服务商免费证书**
1. 阿里云/腾讯云控制台 → **SSL证书** → **免费证书**
2. 申请证书（填写域名 erp.yourdomain.com）
3. 验证域名所有权（DNS验证或文件验证）
4. 下载证书文件（Nginx格式）

**方式二：Let's Encrypt（推荐）**
```bash
# 稍后在服务器上自动配置，更简单
```

---

## 第二步：连接服务器并安装基础环境

### 2.1 连接到服务器

**Windows用户**：
1. 下载 [PuTTY](https://www.putty.org/) 或 [MobaXterm](https://mobaxterm.mobatek.net/)
2. 输入服务器IP、端口22、用户名root、密码
3. 连接成功后进入命令行

**Mac/Linux用户**：
```bash
ssh root@你的服务器IP
# 输入密码后回车
```

### 2.2 更新系统

```bash
# Ubuntu系统
apt update && apt upgrade -y

# CentOS系统
yum update -y
```

### 2.3 安装Docker和Docker Compose

Docker可以让部署变得非常简单，强烈推荐！

```bash
# 安装Docker
curl -fsSL https://get.docker.com | bash

# 启动Docker服务
systemctl start docker
systemctl enable docker

# 验证安装
docker --version

# 安装Docker Compose
curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 验证安装
docker-compose --version
```

### 2.4 安装Nginx（Web服务器）

```bash
# Ubuntu
apt install nginx -y

# CentOS
yum install nginx -y

# 启动Nginx
systemctl start nginx
systemctl enable nginx

# 测试：浏览器访问 http://你的服务器IP
# 应该看到Nginx欢迎页面
```

---

## 第三步：上传项目代码到服务器

### 3.1 在服务器上创建项目目录

```bash
mkdir -p /opt/enterprisehub-erp
cd /opt/enterprisehub-erp
```

### 3.2 上传代码（三种方式任选其一）

**方式一：使用Git（推荐）**
```bash
# 从GitHub克隆项目
git clone https://github.com/chengguo0807/erp.git .

# 如果是私有仓库，需要配置SSH密钥或输入账号密码
```

**方式二：使用SCP上传（从本地上传）**
```bash
# 在你的本地电脑上执行（Windows用WinSCP工具）
scp -r /本地项目路径/enterprisehub-erp root@服务器IP:/opt/
```

**方式三：使用FTP工具**
- 下载 FileZilla 或 WinSCP
- 连接服务器（SFTP协议，端口22）
- 将整个项目文件夹拖拽上传到 `/opt/enterprisehub-erp`

---

## 第四步：配置生产环境参数

### 4.1 修改后端配置文件

```bash
cd /opt/enterprisehub-erp/enterprisehub-erp
vi erp-system/src/main/resources/application-prod.yml
```

创建生产环境配置文件（按 `i` 进入编辑模式）：

```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://mysql:3306/erp_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000

  redis:
    host: redis
    port: 6379
    password: 123456
    database: 0
    timeout: 3000

  # 文件上传限制
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 50MB

# JWT配置
jwt:
  secret: enterprisehub-erp-secret-key-must-be-at-least-256-bits-long
  expiration: 7200  # Token有效期2小时

# 日志配置
logging:
  level:
    root: WARN
    com.enterprisehub: INFO
  file:
    name: /var/log/erp-system.log
    max-size: 100MB
    max-history: 30
```

保存并退出（按 `ESC`，输入 `:wq`，回车）

### 4.2 修改前端配置

```bash
cd /opt/enterprisehub-erp/enterprisehub-erp-ui
vi .env.production
```

创建生产环境变量文件：

```bash
# API地址（通过Nginx代理，使用相对路径）
VITE_API_BASE_URL=/api

# 应用标题
VITE_APP_TITLE=EnterpriseHub ERP管理系统
```

### 4.3 修改Docker Compose配置

```bash
cd /opt/enterprisehub-erp/enterprisehub-erp
vi docker-compose.yml
```

确保配置如下（重点关注密码和端口）：

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: erp-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 你的MySQL密码
      MYSQL_DATABASE: erp_system
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - ./mysql-data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d
    command: --default-authentication-plugin=mysql_native_password

  redis:
    image: redis:7-alpine
    container_name: erp-redis
    restart: always
    command: redis-server --requirepass 你的Redis密码
    ports:
      - "6379:6379"
    volumes:
      - ./redis-data:/data

  erp-system:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: erp-system
    restart: always
    ports:
      - "8082:8082"
    environment:
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      - mysql
      - redis
    volumes:
      - ./logs:/var/log
```

---

## 第五步：构建和启动后端服务

### 5.1 创建后端Dockerfile

```bash
cd /opt/enterprisehub-erp/enterprisehub-erp
vi Dockerfile
```

输入以下内容：

```dockerfile
# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY erp-common ./erp-common
COPY erp-api ./erp-api
COPY erp-system ./erp-system
RUN mvn clean package -DskipTests -pl erp-system -am

# 第二阶段：运行
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/erp-system/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Xms512m", "-Xmx2g", "app.jar"]
```

### 5.2 启动所有服务

```bash
cd /opt/enterprisehub-erp/enterprisehub-erp

# 启动MySQL、Redis、后端服务
docker-compose up -d

# 查看启动日志
docker-compose logs -f

# 等待所有服务启动完成（约2-3分钟）
# 看到 "Started SystemApplication" 表示后端启动成功
```

### 5.3 初始化数据库

```bash
# 进入MySQL容器
docker exec -it erp-mysql mysql -uroot -p你的MySQL密码

# 在MySQL命令行中执行
USE erp_system;
SOURCE /docker-entrypoint-initdb.d/init.sql;
SOURCE /docker-entrypoint-initdb.d/run_all.sql;
EXIT;
```

### 5.4 验证后端服务

```bash
# 测试后端接口
curl http://localhost:8082/api/system/health

# 应该返回类似：{"code":200,"msg":"success"}
```

---

## 第六步：构建和部署前端

### 6.1 在本地构建前端（推荐）

**在你的开发电脑上执行**：

```bash
cd enterprisehub-erp-ui

# 安装依赖
npm install

# 构建生产版本
npm run build

# 构建完成后，dist文件夹就是要部署的静态文件
```

### 6.2 上传前端文件到服务器

```bash
# 在本地电脑执行
scp -r dist/* root@服务器IP:/var/www/erp/

# 或使用FTP工具上传 dist 文件夹内容到服务器 /var/www/erp/
```

**或者在服务器上构建**（需要安装Node.js）：

```bash
# 在服务器上安装Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# 构建前端
cd /opt/enterprisehub-erp/enterprisehub-erp-ui
npm install
npm run build

# 复制构建产物到Nginx目录
mkdir -p /var/www/erp
cp -r dist/* /var/www/erp/
```

---

## 第七步：配置Nginx（关键步骤）

### 7.1 创建Nginx配置文件

```bash
vi /etc/nginx/sites-available/erp
```

输入以下配置：

```nginx
# HTTP服务器（用于重定向到HTTPS）
server {
    listen 80;
    server_name erp.yourdomain.com;  # 改成你的域名
    
    # 重定向到HTTPS
    return 301 https://$server_name$request_uri;
}

# HTTPS服务器
server {
    listen 443 ssl http2;
    server_name erp.yourdomain.com;  # 改成你的域名

    # SSL证书配置（稍后配置Let's Encrypt会自动添加）
    # ssl_certificate /etc/nginx/ssl/cert.pem;
    # ssl_certificate_key /etc/nginx/ssl/key.pem;

    # SSL安全配置
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # 前端静态文件
    root /var/www/erp;
    index index.html;

    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
    gzip_min_length 1000;

    # 前端路由（Vue Router history模式）
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api/ {
        proxy_pass http://localhost:8082/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 文件上传大小限制
    client_max_body_size 50M;

    # 日志
    access_log /var/log/nginx/erp_access.log;
    error_log /var/log/nginx/erp_error.log;
}
```

### 7.2 启用配置并测试

```bash
# 创建软链接启用配置
ln -s /etc/nginx/sites-available/erp /etc/nginx/sites-enabled/

# 测试配置是否正确
nginx -t

# 如果显示 "syntax is ok" 和 "test is successful"，则重启Nginx
systemctl reload nginx
```

### 7.3 配置Let's Encrypt免费SSL证书

```bash
# 安装Certbot
apt install certbot python3-certbot-nginx -y

# 自动配置SSL证书（会自动修改Nginx配置）
certbot --nginx -d erp.yourdomain.com

# 按提示输入：
# 1. 邮箱地址（用于证书到期提醒）
# 2. 同意服务条款：Y
# 3. 是否重定向HTTP到HTTPS：2（推荐）

# 证书会自动续期，测试续期命令
certbot renew --dry-run
```

---

## 第八步：配置防火墙和安全

### 8.1 配置UFW防火墙（Ubuntu）

```bash
# 启用防火墙
ufw enable

# 允许SSH（重要！否则会断开连接）
ufw allow 22/tcp

# 允许HTTP和HTTPS
ufw allow 80/tcp
ufw allow 443/tcp

# 禁止外部直接访问数据库和Redis（安全）
ufw deny 3306/tcp
ufw deny 6379/tcp

# 查看防火墙状态
ufw status
```

### 8.2 修改SSH端口（可选，提高安全性）

```bash
vi /etc/ssh/sshd_config

# 找到 #Port 22，改为：
Port 2222  # 改成其他端口

# 重启SSH服务
systemctl restart sshd

# 记得在防火墙放行新端口
ufw allow 2222/tcp
```

### 8.3 创建非root用户（可选）

```bash
# 创建新用户
adduser erpadmin

# 添加到sudo组
usermod -aG sudo erpadmin

# 后续使用erpadmin登录，避免使用root
```

---

## 第九步：测试和验证

### 9.1 访问系统

1. 打开浏览器，访问：`https://erp.yourdomain.com`
2. 应该看到登录页面
3. 使用默认账号登录：
   - 用户名：`admin`
   - 密码：`admin123`（首次登录后务必修改）

### 9.2 检查服务状态

```bash
# 查看Docker容器状态
docker ps

# 应该看到3个容器在运行：
# - erp-mysql
# - erp-redis
# - erp-system

# 查看后端日志
docker logs -f erp-system

# 查看Nginx日志
tail -f /var/log/nginx/erp_error.log
```

### 9.3 性能测试

```bash
# 测试并发性能（可选）
apt install apache2-utils -y
ab -n 1000 -c 10 https://erp.yourdomain.com/api/system/health
```

---

## 第十步：日常运维

### 10.1 启动/停止服务

```bash
# 停止所有服务
cd /opt/enterprisehub-erp/enterprisehub-erp
docker-compose stop

# 启动所有服务
docker-compose start

# 重启所有服务
docker-compose restart

# 查看服务状态
docker-compose ps
```

### 10.2 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f erp-system

# 查看最近100行日志
docker-compose logs --tail=100 erp-system
```

### 10.3 数据库备份

```bash
# 创建备份目录
mkdir -p /backup/mysql

# 手动备份
docker exec erp-mysql mysqldump -uroot -p你的MySQL密码 erp_system > /backup/mysql/erp_$(date +%Y%m%d_%H%M%S).sql

# 设置自动备份（每天凌晨2点）
crontab -e

# 添加以下行：
0 2 * * * docker exec erp-mysql mysqldump -uroot -p你的MySQL密码 erp_system > /backup/mysql/erp_$(date +\%Y\%m\%d).sql

# 自动删除30天前的备份
0 3 * * * find /backup/mysql -name "erp_*.sql" -mtime +30 -delete
```

### 10.4 更新系统

```bash
# 1. 备份数据库（重要！）
docker exec erp-mysql mysqldump -uroot -p你的MySQL密码 erp_system > /backup/erp_before_update.sql

# 2. 拉取最新代码
cd /opt/enterprisehub-erp
git pull

# 3. 重新构建并启动
cd enterprisehub-erp
docker-compose down
docker-compose up -d --build

# 4. 查看启动日志
docker-compose logs -f
```

### 10.5 监控服务器资源

```bash
# 安装htop（更友好的资源监控工具）
apt install htop -y

# 运行htop查看CPU、内存使用情况
htop

# 查看磁盘使用
df -h

# 查看Docker容器资源使用
docker stats
```

---

## 🚨 常见问题排查

### 问题1：无法访问网站

**检查步骤**：
```bash
# 1. 检查Nginx是否运行
systemctl status nginx

# 2. 检查端口是否监听
netstat -tlnp | grep :80
netstat -tlnp | grep :443

# 3. 检查防火墙
ufw status

# 4. 检查域名解析
ping erp.yourdomain.com

# 5. 查看Nginx错误日志
tail -f /var/log/nginx/erp_error.log
```

### 问题2：后端接口报错

```bash
# 1. 检查后端容器是否运行
docker ps | grep erp-system

# 2. 查看后端日志
docker logs -f erp-system

# 3. 检查数据库连接
docker exec -it erp-mysql mysql -uroot -p

# 4. 检查Redis连接
docker exec -it erp-redis redis-cli -a 你的Redis密码
```

### 问题3：前端页面空白

```bash
# 1. 检查前端文件是否存在
ls -la /var/www/erp/

# 2. 检查Nginx配置
nginx -t

# 3. 查看浏览器控制台错误（F12）

# 4. 检查API代理是否正常
curl http://localhost:8082/api/system/health
```

### 问题4：SSL证书错误

```bash
# 1. 检查证书是否过期
certbot certificates

# 2. 手动续期证书
certbot renew

# 3. 重启Nginx
systemctl reload nginx
```

### 问题5：数据库连接失败

```bash
# 1. 检查MySQL容器是否运行
docker ps | grep mysql

# 2. 查看MySQL日志
docker logs erp-mysql

# 3. 进入MySQL检查
docker exec -it erp-mysql mysql -uroot -p

# 4. 检查数据库是否存在
SHOW DATABASES;
USE erp_system;
SHOW TABLES;
```

---

## 📊 性能优化建议

### 1. 数据库优化

```sql
-- 添加常用查询索引
ALTER TABLE sys_user ADD INDEX idx_username (username);
ALTER TABLE sys_user ADD INDEX idx_status (status);

-- 定期优化表
OPTIMIZE TABLE sys_user;
```

### 2. Redis缓存优化

```yaml
# 在application-prod.yml中配置
spring:
  cache:
    type: redis
    redis:
      time-to-live: 3600000  # 缓存1小时
```

### 3. Nginx优化

```nginx
# 在nginx.conf中添加
worker_processes auto;
worker_connections 2048;

# 开启缓存
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m;
```

### 4. JVM优化

```bash
# 修改Dockerfile中的JVM参数
ENTRYPOINT ["java", "-jar", \
    "-Xms1g", \
    "-Xmx2g", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "app.jar"]
```

---

## 📞 技术支持

如果遇到无法解决的问题：

1. 查看系统日志：`docker-compose logs -f`
2. 查看Nginx日志：`tail -f /var/log/nginx/erp_error.log`
3. 检查服务器资源：`htop` 和 `df -h`
4. 联系技术支持（提供日志文件）

---

## ✅ 部署完成检查清单

- [ ] 域名解析正常（ping通）
- [ ] SSL证书配置成功（https访问正常）
- [ ] 后端服务运行正常（docker ps显示3个容器）
- [ ] 前端页面可以访问
- [ ] 登录功能正常
- [ ] 数据库连接正常
- [ ] Redis缓存正常
- [ ] 防火墙配置正确
- [ ] 数据库自动备份已设置
- [ ] SSL证书自动续期已配置

---

## 🎉 恭喜！

如果以上检查都通过，说明你的ERP系统已经成功部署到生产环境！

现在你可以：
1. 通过 `https://erp.yourdomain.com` 访问系统
2. 创建用户账号，分配角色权限
3. 开始使用ERP系统管理业务

**重要提醒**：
- 定期备份数据库（已设置自动备份）
- 定期更新系统和安全补丁
- 监控服务器资源使用情况
- 首次登录后立即修改默认密码
- 定期查看系统日志，及时发现问题
