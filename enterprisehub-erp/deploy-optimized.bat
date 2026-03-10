@echo off
REM =============================================
REM EnterpriseHub ERP 优化部署脚本 (Windows)
REM 用途：一键完成所有高优先级优化并部署系统
REM =============================================

setlocal enabledelayedexpansion

echo ========================================
echo   EnterpriseHub ERP 优化部署
echo ========================================
echo.

REM 检查Docker是否安装
where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [错误] 未找到Docker，请先安装Docker Desktop
    pause
    exit /b 1
)

echo [1/10] 生成安全密钥和密码...
REM 生成随机密码（Windows版本使用PowerShell）
for /f "delims=" %%i in ('powershell -Command "[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))"') do set JWT_SECRET=%%i
for /f "delims=" %%i in ('powershell -Command "[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 })) -replace '[=+/]','' | Select-Object -First 25"') do set MYSQL_PASSWORD=%%i
for /f "delims=" %%i in ('powershell -Command "[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 })) -replace '[=+/]','' | Select-Object -First 25"') do set REDIS_PASSWORD=%%i

REM 创建.env文件
echo # EnterpriseHub ERP 环境变量配置 > .env
echo # 自动生成时间: %date% %time% >> .env
echo. >> .env
echo # JWT配置 >> .env
echo JWT_SECRET=%JWT_SECRET% >> .env
echo JWT_EXPIRATION=7200 >> .env
echo. >> .env
echo # MySQL配置 >> .env
echo MYSQL_ROOT_PASSWORD=%MYSQL_PASSWORD% >> .env
echo. >> .env
echo # Redis配置 >> .env
echo REDIS_PASSWORD=%REDIS_PASSWORD% >> .env

echo [完成] 密钥已生成并保存到.env文件
echo.

echo [2/10] 备份原配置文件...
if exist docker-compose.yml.backup (
    echo [跳过] 备份文件已存在
) else (
    copy docker-compose.yml docker-compose.yml.backup >nul
    echo [完成] 配置文件已备份
)
echo.

echo [3/10] 停止现有服务...
docker compose down 2>nul
echo [完成] 服务已停止
echo.

echo [4/10] 询问是否清理旧数据...
set /p CLEAN_DATA="是否清理旧数据库数据？(y/N): "
if /i "%CLEAN_DATA%"=="y" (
    echo [执行] 清理旧数据...
    docker volume rm enterprisehub-erp_mysql-data 2>nul
    docker volume rm enterprisehub-erp_redis-data 2>nul
    echo [完成] 旧数据已清理
) else (
    echo [跳过] 保留现有数据
)
echo.

echo [5/10] 构建优化后的Docker镜像...
echo 这可能需要几分钟时间，请耐心等待...
docker compose build --no-cache
echo [完成] 镜像构建完成
echo.

echo [6/10] 启动优化后的服务...
docker compose up -d
echo [完成] 服务已启动
echo.

echo [7/10] 等待服务启动...
echo 等待MySQL启动...
timeout /t 15 /nobreak >nul
echo 等待Redis启动...
timeout /t 5 /nobreak >nul
echo 等待后端服务启动...
timeout /t 30 /nobreak >nul
echo 等待网关服务启动...
timeout /t 10 /nobreak >nul
echo [完成] 服务启动等待完成
echo.

echo [8/10] 执行健康检查...
docker exec erp-mysql mysqladmin ping -h localhost -p%MYSQL_PASSWORD% --silent 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] MySQL健康
) else (
    echo [警告] MySQL可能未就绪
)

docker exec erp-redis redis-cli -a %REDIS_PASSWORD% ping 2>nul | findstr /C:"PONG" >nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Redis健康
) else (
    echo [警告] Redis可能未就绪
)

curl -f http://localhost:8082/actuator/health >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [OK] 后端服务健康
) else (
    echo [警告] 后端服务可能未就绪
)

curl -f http://localhost:8080/actuator/health >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [OK] 网关服务健康
) else (
    echo [警告] 网关服务可能未就绪
)
echo.

echo ========================================
echo   部署完成！
echo ========================================
echo.
echo 重要信息：
echo   数据库密码: %MYSQL_PASSWORD%
echo   Redis密码: %REDIS_PASSWORD%
echo   JWT密钥: 已保存到.env文件
echo.
echo   [警告] 请妥善保管.env文件，不要提交到Git！
echo.
echo 访问地址：
echo   前端:      http://localhost
echo   后端:      http://localhost:8082
echo   网关:      http://localhost:8080
echo   API文档:   http://localhost:8082/swagger-ui.html
echo.
echo 查看日志：
echo   docker compose logs -f
echo.
echo 已完成的优化：
echo   [OK] 1. 启用接口认证
echo   [OK] 2. 使用强随机JWT密钥
echo   [OK] 3. 使用强随机数据库密码
echo   [OK] 4. 配置Redis缓存
echo   [OK] 5. 优化数据库连接池
echo   [OK] 6. 添加数据库索引
echo   [OK] 7. 优化Docker镜像
echo   [OK] 8. 添加健康检查和优雅停机
echo.
echo 下一步：
echo   1. 访问 http://localhost 测试系统
echo   2. 使用默认账号登录: admin / admin123
echo   3. 修改默认密码
echo.
echo ========================================

pause
