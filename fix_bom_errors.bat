@echo off
chcp 65001 >nul
echo 正在修复BOM编码错误...
echo.

REM 修复所有有BOM错误的文件
powershell -Command "$files = @('enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\WarehouseController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\StockOutController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\CustomerController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\service\impl\StockInServiceImpl.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\ProductController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\SysMenuController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\PaymentStatisticsController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\PaymentController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\PurchaseStatisticsController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\SysDeptController.java', 'enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller\ProductCategoryController.java'); foreach ($file in $files) { if (Test-Path $file) { $content = Get-Content $file -Raw -Encoding UTF8; $content = $content.TrimStart([char]0xFEFF); [System.IO.File]::WriteAllText((Resolve-Path $file).Path, $content, (New-Object System.Text.UTF8Encoding $false)); Write-Host \"已修复: $file\" -ForegroundColor Yellow } }"

echo.
echo 修复完成!
echo.
echo 请运行以下命令重新编译:
echo cd enterprisehub-erp
echo mvn clean compile -DskipTests
echo.
pause
