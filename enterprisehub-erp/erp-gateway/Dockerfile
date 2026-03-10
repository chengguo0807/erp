# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom文件
COPY pom.xml .
COPY erp-common/pom.xml erp-common/
COPY erp-gateway/pom.xml erp-gateway/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY erp-common/src erp-common/src
COPY erp-gateway/src erp-gateway/src

# 构建应用
RUN mvn clean package -DskipTests -pl erp-gateway -am

# 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制jar文件
COPY --from=builder /app/erp-gateway/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /var/log

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", \
    "-Xms256m", \
    "-Xmx512m", \
    "-Dspring.profiles.active=docker", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "app.jar"]