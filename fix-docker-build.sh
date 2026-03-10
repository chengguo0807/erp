#!/bin/bash

# ERP系统Docker构建问题修复脚本
# 使用方法: bash fix-docker-build.sh

echo "=== ERP系统Docker构建问题修复脚本 ==="
echo ""

# 检查当前目录
if [ ! -f "docker-compose.yml" ]; then
    echo "错误: 请在包含docker-compose.yml的目录中运行此脚本"
    echo "正确路径应该是: /opt/enterprisehub-erp/erp/enterprisehub-erp/"
    exit 1
fi

echo "1. 停止所有运行中的容器..."
docker compose down

echo ""
echo "2. 清理Docker缓存..."
docker system prune -f

echo ""
echo "3. 分步构建服务..."

echo "   3.1 构建MySQL和Redis..."
docker compose build mysql redis
if [ $? -ne 0 ]; then
    echo "错误: MySQL/Redis构建失败"
    exit 1
fi

echo "   3.2 启动MySQL和Redis..."
docker compose up -d mysql redis

echo "   3.3 等待数据库启动..."
sleep 10

echo "   3.4 构建erp-system..."
docker compose build --no-cache erp-system
if [ $? -ne 0 ]; then
    echo "错误: erp-system构建失败，请检查Dockerfile"
    echo "常见问题: COPY路径不正确"
    exit 1
fi

echo "   3.5 启动erp-system..."
docker compose up -d erp-system

echo "   3.6 构建erp-gateway..."
docker compose build --no-cache erp-gateway
if [ $? -ne 0 ]; then
    echo "错误: erp-gateway构建失败，请检查Dockerfile"
    exit 1
fi

echo "   3.7 启动erp-gateway..."
docker compose up -d erp-gateway

echo "   3.8 构建前端（如果存在）..."
if docker compose config --services | grep -q "erp-frontend"; then
    docker compose build --no-cache erp-frontend
    if [ $? -eq 0 ]; then
        docker compose up -d erp-frontend
        echo "   前端构建成功"
    else
        echo "   警告: 前端构建失败，但不影响后端服务"
    fi
else
    echo "   跳过前端构建（服务未配置）"
fi

echo ""
echo "4. 检查服务状态..."
docker compose ps

echo ""
echo "5. 检查服务日志..."
echo "   如果看到错误，请运行: docker compose logs -f [服务名]"

echo ""
echo "=== 修复完成 ==="
echo "访问测试: curl http://localhost:8082/actuator/health"
echo "查看日志: docker compose logs -f erp-system"