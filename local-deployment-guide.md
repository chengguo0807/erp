# ERP系统本地部署指南（Ubuntu服务器）

由于网络限制无法使用Docker，我们采用本地安装的方式部署ERP系统。

## 第一步：安装MySQL 8.0

```bash
# 更新系统包
sudo apt update

# 安装MySQL 8.0
sudo apt install mysql-server -y

# 启动MySQL服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 配置MySQL安全设置
sudo mysql_secure_installation
# 按提示操作：
# 1. 设置root密码：123456
# 2. 移除匿名用户：Y
# 3. 禁止root远程登录：N（我们需要本地连接）
# 4. 移除测试数据库：Y
# 5. 重新加载权限表：Y

# 登录MySQL创建数据库
sudo mysql -u root -p
# 输入密码：123456

# 在MySQL命令行中执行：
CREATE DATABASE erp_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON erp_system.* TO 'root'@'localhost' IDENTIFIED BY '123456';
FLUSH PRIVILEGES;
EXIT;
```

## 第二步：安装Redis

```bash
# 安装Redis
sudo apt install redis-server -y

# 配置Redis密码
sudo vi /etc/redis/redis.conf

# 找到 # requirepass foobared 这一行，取消注释并修改为：
requirepass 123456

# 重启Redis服务
sudo systemctl restart redis-server
sudo systemctl enable redis-server

# 测试Redis连接
redis-cli -a 123456 ping
# 应该返回 PONG
```

## 第三步：安装Java 17

```bash
# 安装OpenJDK 17
sudo apt install openjdk-17-jdk -y

# 验证安装
java -version
javac -version

# 设置JAVA_HOME环境变量
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
```

## 第四步：安装Maven

```bash
# 安装Maven
sudo apt install maven -y

# 验证安装
mvn -version
```

## 第五步：导入数据库结构

```bash
# 进入项目目录
cd /opt/enterprisehub-erp/erp/enterprisehub-erp

# 导入数据库初始化脚本（如果存在）
mysql -u root -p123456 erp_system < sql/init.sql

# 如果没有init.sql文件，需要手动创建基础表结构
# 可以从项目的其他SQL文件中导入
```

## 第六步：编译和启动后端服务

```bash
# 进入后端项目目录
cd /opt/enterprisehub-erp/erp/enterprisehub-erp

# 创建日志目录
mkdir -p /opt/enterprisehub-erp/erp/logs

# 编译项目（跳过测试）
mvn clean package -DskipTests

# 启动erp-system服务
cd erp-system
nohup java -jar -Dspring.profiles.active=local target/erp-system-*.jar > /opt/enterprisehub-erp/erp/logs/erp-system.log 2>&1 &

# 查看启动日志
tail -f /opt/enterprisehub-erp/erp/logs/erp-system.log

# 等待看到 "Started SystemApplication" 表示启动成功
```

## 第七步：验证服务

```bash
# 检查服务是否启动
ps aux | grep java

# 检查端口是否监听
netstat -tlnp | grep 8082

# 测试API接口
curl http://localhost:8082/actuator/health
# 或者
curl http://localhost:8082/api/system/info
```

## 第八步：安装和配置Nginx

```bash
# 安装Nginx
sudo apt install nginx -y

# 创建前端目录
sudo mkdir -p /var/www/erp

# 启动Nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

## 第九步：构建前端（如果需要）

```bash
# 安装Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 进入前端项目目录
cd /opt/enterprisehub-erp/erp/enterprisehub-erp-ui

# 创建生产环境配置
cat > .env.production << EOF
VITE_API_BASE_URL=/api
VITE_APP_TITLE=EnterpriseHub ERP管理系统
EOF

# 安装依赖并构建
npm install
npm run build

# 复制构建产物到Nginx目录
sudo cp -r dist/* /var/www/erp/
```

## 第十步：配置Nginx代理

```bash
# 创建Nginx配置文件
sudo vi /etc/nginx/sites-available/erp

# 输入以下配置：
server {
    listen 80;
    server_name localhost;

    root /var/www/erp;
    index index.html;

    # 前端路由
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
    }

    client_max_body_size 50M;
}

# 启用配置
sudo ln -s /etc/nginx/sites-available/erp /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

## 服务管理脚本

创建服务管理脚本方便日常操作：

```bash
# 创建启动脚本
cat > /opt/enterprisehub-erp/erp/start-erp.sh << 'EOF'
#!/bin/bash
cd /opt/enterprisehub-erp/erp/enterprisehub-erp

echo "启动MySQL..."
sudo systemctl start mysql

echo "启动Redis..."
sudo systemctl start redis-server

echo "启动ERP后端服务..."
cd erp-system
nohup java -jar -Dspring.profiles.active=local target/erp-system-*.jar > /opt/enterprisehub-erp/erp/logs/erp-system.log 2>&1 &

echo "启动Nginx..."
sudo systemctl start nginx

echo "ERP系统启动完成！"
echo "访问地址: http://服务器IP"
EOF

chmod +x /opt/enterprisehub-erp/erp/start-erp.sh

# 创建停止脚本
cat > /opt/enterprisehub-erp/erp/stop-erp.sh << 'EOF'
#!/bin/bash
echo "停止ERP后端服务..."
pkill -f "erp-system"

echo "停止Nginx..."
sudo systemctl stop nginx

echo "ERP系统已停止！"
EOF

chmod +x /opt/enterprisehub-erp/erp/stop-erp.sh
```

## 故障排查

### 1. 检查MySQL连接
```bash
mysql -u root -p123456 -e "SHOW DATABASES;"
```

### 2. 检查Redis连接
```bash
redis-cli -a 123456 ping
```

### 3. 检查Java进程
```bash
ps aux | grep java
jps -l
```

### 4. 查看日志
```bash
tail -f /opt/enterprisehub-erp/erp/logs/erp-system.log
```

### 5. 检查端口占用
```bash
netstat -tlnp | grep -E "(3306|6379|8082|80)"
```

## 开机自启动设置

```bash
# 创建systemd服务文件
sudo vi /etc/systemd/system/erp-system.service

# 输入以下内容：
[Unit]
Description=ERP System Service
After=mysql.service redis-server.service

[Service]
Type=forking
User=root
WorkingDirectory=/opt/enterprisehub-erp/erp/enterprisehub-erp/erp-system
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=local target/erp-system-*.jar
Restart=always

[Install]
WantedBy=multi-user.target

# 启用服务
sudo systemctl daemon-reload
sudo systemctl enable erp-system.service
```

这样配置后，ERP系统就可以在本地环境中正常运行了！