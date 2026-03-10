# 修复编译错误的PowerShell脚本
# 用法: .\fix_compilation_errors.ps1

Write-Host "开始修复编译错误..." -ForegroundColor Green

# 定义Controller目录
$controllerDir = "e:\ERP\enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\controller"

# 获取所有Controller文件
$files = Get-ChildItem -Path $controllerDir -Filter "*.java" -Recurse

$totalFiles = 0
$modifiedFiles = 0

foreach ($file in $files) {
    $totalFiles++
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content
    
    # 修复@OperationLog注解
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "新增"\)', '@OperationLog(module = "$1", businessType = 1)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "修改"\)', '@OperationLog(module = "$1", businessType = 2)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "删除"\)', '@OperationLog(module = "$1", businessType = 3)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "审核"\)', '@OperationLog(module = "$1", businessType = 1)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "完成"\)', '@OperationLog(module = "$1", businessType = 2)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "关闭"\)', '@OperationLog(module = "$1", businessType = 3)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "调拨"\)', '@OperationLog(module = "$1", businessType = 1)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "分配权限"\)', '@OperationLog(module = "$1", businessType = 2)'
    $content = $content -replace '@OperationLog\(title = "([^"]+)", businessType = "批量删除"\)', '@OperationLog(module = "$1", businessType = 3)'
    
    if ($content -ne $originalContent) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        Write-Host "已修复: $($file.Name)" -ForegroundColor Yellow
        $modifiedFiles++
    }
}

Write-Host "`n修复Controller文件完成: 共检查 $totalFiles 个文件, 修改了 $modifiedFiles 个文件" -ForegroundColor Green

# 修复StockInServiceImpl
Write-Host "`n开始修复StockInServiceImpl..." -ForegroundColor Green
$stockInServiceFile = "e:\ERP\enterprisehub-erp\erp-system\src\main\java\com\enterprisehub\system\service\impl\StockInServiceImpl.java"

if (Test-Path $stockInServiceFile) {
    $content = Get-Content $stockInServiceFile -Raw -Encoding UTF8
    
    # 修复方法调用
    $content = $content -replace 'StockInItem::getStockInId', 'StockInItem::getId'
    $content = $content -replace '\.getStockInNo\(\)', '.getStockInNo()'
    $content = $content -replace '\.getStatus\(\)', '.getStatus()'
    $content = $content -replace 'stockIn\.setItems', 'stockIn.setItems'
    $content = $content -replace 'stockIn\.getItems', 'stockIn.getItems'
    $content = $content -replace 'item\.setStockInId', 'item.setStockInId'
    $content = $content -replace 'stockIn\.setStatus', 'stockIn.setStatus'
    
    Set-Content -Path $stockInServiceFile -Value $content -Encoding UTF8 -NoNewline
    Write-Host "已修复: StockInServiceImpl.java" -ForegroundColor Yellow
} else {
    Write-Host "未找到StockInServiceImpl.java文件" -ForegroundColor Red
}

Write-Host "`n所有修复完成!" -ForegroundColor Green
Write-Host "请运行以下命令重新编译:" -ForegroundColor Cyan
Write-Host "cd enterprisehub-erp" -ForegroundColor Cyan
Write-Host "mvn clean compile -DskipTests" -ForegroundColor Cyan
