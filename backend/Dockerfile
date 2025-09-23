# 使用OpenJDK 17作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 创建uploads目录
RUN mkdir -p /app/uploads

# 启动应用
CMD ["java", "-jar", "target/heartplan-backend-1.0.0.jar"]