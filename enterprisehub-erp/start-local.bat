@echo off
chcp 65001 >nul
REM 本地开发启动脚本 - 仅启动数据库，后端本地运行

echo ========================================
echo   ERP本地开发环境启动
echo ========================================
echo.

echo [1/4] 启动MySQL和Redis...
docker compose -f docker-compose-db-only.yml up -d
echo.

echo [2/4] 等待数据库启动...
timeout /t 15 /nobreak >nul
echo.

echo [3/4] 检查服务状态...
docker compose -f docker-compose-db-only.yml ps
echo.

echo [4/4] 数据库已就绪！
echo.
echo ========================================
echo   数据库启动完成
echo ========================================
echo.
echo MySQL连接信息：
echo   地址: localhost:3306
echo   数据库: erp_system
echo   用户名: root
echo   密码: 123456
echo.
echo Redis连接信息：
echo   地址: localhost:6379
echo   密码: 123456
echo.
echo 下一步：
echo   1. 在IDEA中运行 SystemApplication.java
echo   2. 或执行: mvn spring-boot:run
echo   3. 访问: http://localhost:8082
echo.
echo 停止数据库：
echo   docker compose -f docker-compose-db-only.yml down
echo.
echo ========================================

pause
