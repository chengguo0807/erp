# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制所有pom文件（包括子模块）
COPY pom.xml .
COPY erp-common/pom.xml erp-common/
COPY erp-common/erp-common-core/pom.xml erp-common/erp-common-core/
COPY erp-common/erp-common-log/pom.xml erp-common/erp-common-log/
COPY erp-common/erp-common-mybatis/pom.xml erp-common/erp-common-mybatis/
COPY erp-common/erp-common-redis/pom.xml erp-common/erp-common-redis/
COPY erp-common/erp-common-security/pom.xml erp-common/erp-common-security/
COPY erp-api/pom.xml erp-api/
COPY erp-api/erp-api-system/pom.xml erp-api/erp-api-system/
COPY erp-system/pom.xml erp-system/

# 下载依赖（利用Docker缓存）
RUN mvn dependency:go-offline -B

# 复制所有源代码
COPY erp-common ./erp-common
COPY erp-api ./erp-api
COPY erp-system ./erp-system

# 构建应用
RUN mvn clean package -DskipTests -pl erp-system -am

# 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine

# 安装必要工具（用于健康检查）
RUN apk add --no-cache tzdata wget && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 设置工作目录
WORKDIR /app

# 复制jar文件
COPY --from=builder /app/erp-system/target/*.jar app.jar

# 创建非root用户
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    mkdir -p /var/log && \
    chown -R appuser:appuser /app /var/log

USER appuser

# 暴露端口
EXPOSE 8082

# JVM优化参数
ENTRYPOINT ["java", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UseStringDeduplication", \
    "-Xms512m", \
    "-Xmx1g", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]
