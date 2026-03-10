# 部署问题排查指南

## 问题1：Docker镜像拉取失败

### 错误信息
```
failed to resolve source metadata for docker.io/library/maven:3.9-eclipse-temurin-17
failed to do request: Head "https://registry-1.docker.io/v2/library/maven/manifests/3.9-eclipse-temurin-17": EOF
```

### 原因
网络无法访问Docker Hub（registry-1.docker.io）

### 解决方案

#### 方案1：配置Docker镜像加速器（推荐）

1. 打开Docker Desktop设置
2. 进入 Settings → Docker Engine
3. 添加国内镜像源：

```json
{
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com"
  ]
}
```

4. 点击 Apply & Restart
5. 重新运行部署脚本

#### 方案2：使用本地构建（无需Docker Hub）

如果网络问题无法解决，使用本地Maven构建：

```bash
# 1. 本地编译
cd enterprisehub-erp
mvn clean package -DskipTests

# 2. 使用简化的Dockerfile（不需要Maven镜像）
# 见下方"简化Dockerfile"部分

# 3. 手动启动服务
docker compose up -d mysql redis
# 等待数据库启动后
java -jar erp-system/target/erp-system-*.jar
```

#### 方案3：使用阿里云镜像

修改Dockerfile，使用阿里云Maven镜像：
```dockerfile
FROM registry.cn-hangzhou.aliyuncs.com/acs/maven:3.9-eclipse-temurin-17 AS builder
```

---

## 问题2：批处理脚本中文乱码

### 原因
Windows批处理文件编码问题

### 解决方案

使用PowerShell脚本代替（已创建）：

```powershell
cd enterprisehub-erp
.\deploy-optimized.ps1
```

或者手动执行步骤：

```cmd
# 1. 创建.env文件
copy .env.example .env
# 手动编辑.env文件，填入密码

# 2. 停止旧服务
docker compose down

# 3. 启动服务（跳过构建）
docker compose up -d mysql redis

# 4. 等待数据库启动
timeout /t 20

# 5. 查看服务状态
docker compose ps
docker compose logs
```

---

## 问题3：服务未就绪

### 检查步骤

```cmd
# 1. 查看运行中的容器
docker compose ps

# 2. 查看日志
docker compose logs mysql
docker compose logs redis

# 3. 检查网络
docker network ls
docker network inspect enterprisehub-erp_erp-network

# 4. 手动测试连接
docker exec -it erp-mysql mysql -uroot -p
docker exec -it erp-redis redis-cli -a 你的密码
```

---

## 快速修复：跳过Docker构建

如果Docker构建一直失败，可以先启动数据库，然后本地运行后端：

### 步骤1：启动数据库

```yaml
# 创建 docker-compose-db-only.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: erp-mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: erp_system
    volumes:
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
      - ./sql/add_indexes.sql:/docker-entrypoint-initdb.d/add_indexes.sql
    command: --character-set-server=utf8mb4

  redis:
    image: redis:7-alpine
    container_name: erp-redis
    ports:
      - "6379:6379"
    command: redis-server --requirepass 123456
```

```cmd
docker compose -f docker-compose-db-only.yml up -d
```

### 步骤2：本地运行后端

```cmd
cd enterprisehub-erp

# 编译
mvn clean package -DskipTests

# 运行
cd erp-system
java -jar -Dspring.profiles.active=local target/erp-system-*.jar
```

### 步骤3：运行前端

```cmd
cd enterprisehub-erp-ui
npm install
npm run dev
```

---

## 常见问题FAQ

### Q: 如何查看生成的密码？
A: 查看.env文件：
```cmd
type .env
```

### Q: 如何重置所有数据？
A: 
```cmd
docker compose down -v
docker volume prune -f
```

### Q: 如何只重启某个服务？
A: 
```cmd
docker compose restart erp-system
docker compose restart mysql
```

### Q: 如何进入容器调试？
A: 
```cmd
docker exec -it erp-system sh
docker exec -it erp-mysql bash
```

### Q: 端口被占用怎么办？
A: 
```cmd
# 查看端口占用
netstat -ano | findstr :3306
netstat -ano | findstr :8082

# 修改docker-compose.yml中的端口映射
ports:
  - "13306:3306"  # 改用其他端口
```

---

## 联系支持

如果以上方法都无法解决问题，请提供：
1. `docker compose ps` 输出
2. `docker compose logs` 输出
3. `.env` 文件内容（隐藏密码）
4. 网络测试结果：`ping registry-1.docker.io`
