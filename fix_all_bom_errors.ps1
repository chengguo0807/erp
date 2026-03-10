# 修复所有Java文件的BOM编码问题
Write-Host "开始扫描并修复BOM编码错误..." -ForegroundColor Cyan
Write-Host ""

$rootPath = "enterprisehub-erp\erp-system\src\main\java"
$fixedCount = 0
$totalCount = 0

# 获取所有Java文件
$javaFiles = Get-ChildItem -Path $rootPath -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    $totalCount++
    
    # 读取文件内容
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    
    # 检查是否包含BOM
    if ($content.Length -gt 0 -and $content[0] -eq [char]0xFEFF) {
        # 移除BOM
        $content = $content.TrimStart([char]0xFEFF)
        
        # 写回文件（不带BOM的UTF-8）
        [System.IO.File]::WriteAllText($file.FullName, $content, (New-Object System.Text.UTF8Encoding $false))
        
        Write-Host "已修复: $($file.Name)" -ForegroundColor Green
        $fixedCount++
    }
}

Write-Host ""
Write-Host "扫描完成!" -ForegroundColor Cyan
Write-Host "总文件数: $totalCount" -ForegroundColor Yellow
Write-Host "修复文件数: $fixedCount" -ForegroundColor Green
Write-Host ""

if ($fixedCount -gt 0) {
    Write-Host "请运行以下命令重新编译:" -ForegroundColor Cyan
    Write-Host "cd enterprisehub-erp" -ForegroundColor White
    Write-Host "mvn clean compile -DskipTests" -ForegroundColor White
} else {
    Write-Host "没有发现BOM编码问题" -ForegroundColor Green
}

Write-Host ""
