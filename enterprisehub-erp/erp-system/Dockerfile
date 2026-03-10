# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom文件
COPY pom.xml .
COPY erp-common/pom.xml erp-common/
COPY erp-system/pom.xml erp-system/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY erp-common/src erp-common/src
COPY erp-system/src erp-system/src

# 构建应用
RUN mvn clean package -DskipTests -pl erp-system -am

# 第二阶段：运行
FROM eclipse-temurin:17-jre-alpine

# 设置工作目录
WORKDIR /app

# 复制jar文件
COPY --from=builder /app/erp-system/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /var/log

# 暴露端口
EXPOSE 8082

# 启动应用
ENTRYPOINT ["java", "-jar", \
    "-Xms512m", \
    "-Xmx1g", \
    "-Dspring.profiles.active=docker", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "app.jar"]