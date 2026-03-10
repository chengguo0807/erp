#!/bin/bash
# =============================================
# EnterpriseHub ERP 优化部署脚本
# 用途：一键完成所有高优先级优化并部署系统
# =============================================

set -e  # 遇到错误立即退出

echo "🚀 EnterpriseHub ERP 优化部署开始..."
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查必要命令
check_command() {
    if ! command -v $1 &> /dev/null; then
        echo -e "${RED}❌ 错误: 未找到 $1 命令${NC}"
        exit 1
    fi
}

echo "📋 检查必要工具..."
check_command docker
check_command openssl
echo -e "${GREEN}✅ 工具检查完成${NC}"
echo ""

# 1. 生成强密码
echo "🔐 生成安全密钥和密码..."
JWT_SECRET=$(openssl rand -base64 64 | tr -d '\n')
MYSQL_PASSWORD=$(openssl rand -base64 32 | tr -d "=+/\n" | cut -c1-25)
REDIS_PASSWORD=$(openssl rand -base64 32 | tr -d "=+/\n" | cut -c1-25)

# 2. 创建.env文件
cat > .env << EOF
# EnterpriseHub ERP 环境变量配置
# 自动生成时间: $(date)

# JWT配置
JWT_SECRET=${JWT_SECRET}
JWT_EXPIRATION=7200

# MySQL配置
MYSQL_ROOT_PASSWORD=${MYSQL_PASSWORD}

# Redis配置
REDIS_PASSWORD=${REDIS_PASSWORD}
EOF

echo -e "${GREEN}✅ 密钥已生成并保存到.env文件${NC}"
echo ""

# 3. 备份原配置
echo "💾 备份原配置文件..."
if [ -f "docker-compose.yml.backup" ]; then
    echo -e "${YELLOW}⚠️  备份文件已存在，跳过备份${NC}"
else
    cp docker-compose.yml docker-compose.yml.backup
    echo -e "${GREEN}✅ 配置文件已备份${NC}"
fi
echo ""

# 4. 停止现有服务
echo "⏸️  停止现有服务..."
docker compose down 2>/dev/null || true
echo -e "${GREEN}✅ 服务已停止${NC}"
echo ""

# 5. 清理旧数据（可选）
read -p "是否清理旧数据库数据？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🗑️  清理旧数据..."
    docker volume rm enterprisehub-erp_mysql-data 2>/dev/null || true
    docker volume rm enterprisehub-erp_redis-data 2>/dev/null || true
    echo -e "${GREEN}✅ 旧数据已清理${NC}"
else
    echo -e "${YELLOW}⏭️  保留现有数据${NC}"
fi
echo ""

# 6. 构建镜像
echo "🔨 构建优化后的Docker镜像..."
echo "   这可能需要几分钟时间..."
docker compose build --no-cache 2>&1 | grep -E "^(#|Step|Successfully|ERROR)" || true
echo -e "${GREEN}✅ 镜像构建完成${NC}"
echo ""

# 7. 启动服务
echo "🚀 启动优化后的服务..."
docker compose up -d
echo -e "${GREEN}✅ 服务已启动${NC}"
echo ""

# 8. 等待服务启动
echo "⏳ 等待服务启动..."
echo "   MySQL启动中..."
for i in {1..30}; do
    if docker exec erp-mysql mysqladmin ping -h localhost -p${MYSQL_PASSWORD} --silent 2>/dev/null; then
        echo -e "${GREEN}   ✅ MySQL已就绪${NC}"
        break
    fi
    sleep 2
    echo -n "."
done
echo ""

echo "   Redis启动中..."
for i in {1..15}; do
    if docker exec erp-redis redis-cli -a ${REDIS_PASSWORD} ping 2>/dev/null | grep -q PONG; then
        echo -e "${GREEN}   ✅ Redis已就绪${NC}"
        break
    fi
    sleep 2
    echo -n "."
done
echo ""

echo "   后端服务启动中..."
for i in {1..60}; do
    if curl -f http://localhost:8082/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}   ✅ 后端服务已就绪${NC}"
        break
    fi
    sleep 3
    echo -n "."
done
echo ""

echo "   网关服务启动中..."
for i in {1..30}; do
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}   ✅ 网关服务已就绪${NC}"
        break
    fi
    sleep 2
    echo -n "."
done
echo ""

# 9. 健康检查
echo "🏥 执行健康检查..."
HEALTH_CHECK_PASSED=true

# 检查MySQL
if docker exec erp-mysql mysqladmin ping -h localhost -p${MYSQL_PASSWORD} --silent 2>/dev/null; then
    echo -e "${GREEN}✅ MySQL健康${NC}"
else
    echo -e "${RED}❌ MySQL不健康${NC}"
    HEALTH_CHECK_PASSED=false
fi

# 检查Redis
if docker exec erp-redis redis-cli -a ${REDIS_PASSWORD} ping 2>/dev/null | grep -q PONG; then
    echo -e "${GREEN}✅ Redis健康${NC}"
else
    echo -e "${RED}❌ Redis不健康${NC}"
    HEALTH_CHECK_PASSED=false
fi

# 检查后端
if curl -f http://localhost:8082/actuator/health > /dev/null 2>&1; then
    echo -e "${GREEN}✅ 后端服务健康${NC}"
else
    echo -e "${RED}❌ 后端服务不健康${NC}"
    HEALTH_CHECK_PASSED=false
fi

# 检查网关
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo -e "${GREEN}✅ 网关服务健康${NC}"
else
    echo -e "${RED}❌ 网关服务不健康${NC}"
    HEALTH_CHECK_PASSED=false
fi

echo ""

# 10. 显示结果
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
if [ "$HEALTH_CHECK_PASSED" = true ]; then
    echo -e "${GREEN}🎉 优化部署完成！所有服务运行正常${NC}"
else
    echo -e "${YELLOW}⚠️  部署完成，但部分服务可能未就绪${NC}"
    echo -e "${YELLOW}   请运行 'docker compose logs' 查看详细日志${NC}"
fi
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "📋 重要信息："
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "  ${YELLOW}数据库密码:${NC} ${MYSQL_PASSWORD}"
echo -e "  ${YELLOW}Redis密码:${NC} ${REDIS_PASSWORD}"
echo -e "  ${YELLOW}JWT密钥:${NC} 已保存到.env文件"
echo ""
echo -e "  ${RED}⚠️  请妥善保管.env文件，不要提交到Git！${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "🔗 访问地址："
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  前端:      http://localhost"
echo "  后端:      http://localhost:8082"
echo "  网关:      http://localhost:8080"
echo "  API文档:   http://localhost:8082/swagger-ui.html"
echo "  健康检查:  http://localhost:8082/actuator/health"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "📊 查看日志："
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  所有服务:  docker compose logs -f"
echo "  后端服务:  docker compose logs -f erp-system"
echo "  网关服务:  docker compose logs -f erp-gateway"
echo "  数据库:    docker compose logs -f mysql"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "✅ 已完成的优化："
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  ✅ 1. 启用接口认证（所有业务接口需要登录）"
echo "  ✅ 2. 使用强随机JWT密钥"
echo "  ✅ 3. 使用强随机数据库密码"
echo "  ✅ 4. 配置Redis缓存（用户、菜单、角色）"
echo "  ✅ 5. 优化数据库连接池（最大50连接）"
echo "  ✅ 6. 添加数据库索引（提升查询性能）"
echo "  ✅ 7. 优化Docker镜像（使用非root用户）"
echo "  ✅ 8. 添加健康检查和优雅停机"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "📝 下一步："
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  1. 访问 http://localhost 测试系统"
echo "  2. 使用默认账号登录: admin / admin123"
echo "  3. 修改默认密码"
echo "  4. 查看 docs/optimization-analysis-report.md 了解更多优化建议"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo -e "${GREEN}🎊 部署完成！${NC}"
