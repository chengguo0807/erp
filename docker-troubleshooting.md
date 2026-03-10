# Docker部署故障排查快速指南

## 🚨 常见问题及解决方案

### 1. 构建失败：找不到文件或目录

**错误信息**：
```
COPY erp-common/src: no such file or directory
```

**原因**：Dockerfile中的COPY路径不正确，erp-common是多模块项目

**解决方案**：
```bash
# 方法1：使用修复脚本
cd /opt/enterprisehub-erp/erp/enterprisehub-erp
bash fix-docker-build.sh

# 方法2：手动修复
docker compose down
docker system prune -f
docker compose build --no-cache
docker compose up -d
```

### 2. 网络连接问题

**错误信息**：
```
curl: (35) OpenSSL SSL_connect: Connection reset by peer
100% packet loss to registry-1.docker.io
```

**解决方案**：
```bash
# 测试网络连接
ping registry-1.docker.io

# 如果网络受限，使用本地安装方式
# 参考 local-deployment-guide.md
```

### 3. 容器启动失败

**检查步骤**：
```bash
# 1. 查看容器状态
docker compose ps

# 2. 查看失败容器的日志
docker compose logs [服务名]

# 3. 检查端口占用
netstat -tlnp | grep -E "(3306|6379|8082|8080)"

# 4. 重启服务
docker compose restart [服务名]
```

### 4. 数据库连接失败

**解决方案**：
```bash
# 1. 检查MySQL容器
docker compose logs mysql

# 2. 进入MySQL容器测试
docker exec -it erp-mysql mysql -uroot -p123456

# 3. 检查数据库
SHOW DATABASES;
USE erp_system;
```

### 5. 前端页面空白

**解决方案**：
```bash
# 1. 检查前端容器
docker compose logs erp-frontend

# 2. 检查Nginx配置
docker exec -it erp-frontend nginx -t

# 3. 检查API代理
curl http://localhost:8082/actuator/health
```

## 🔧 快速命令参考

```bash
# 查看所有服务状态
docker compose ps

# 查看服务日志
docker compose logs -f [服务名]

# 重启服务
docker compose restart [服务名]

# 重新构建服务
docker compose build --no-cache [服务名]

# 停止所有服务
docker compose down

# 清理Docker缓存
docker system prune -f

# 完全重新部署
docker compose down
docker system prune -f
docker compose up -d --build
```

## 📞 获取帮助

如果问题仍然存在：

1. 收集日志信息：
   ```bash
   docker compose logs > docker-logs.txt
   ```

2. 检查系统资源：
   ```bash
   df -h
   free -h
   docker stats
   ```

3. 提供以下信息寻求帮助：
   - 错误日志
   - 系统配置
   - Docker版本：`docker --version`
   - Docker Compose版本：`docker compose version`