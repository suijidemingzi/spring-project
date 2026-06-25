# 第一阶段：构建阶段（使用维护中的 Temurin JDK 镜像）
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段（使用轻量级的 Temurin JRE 镜像）
FROM eclipse-temurin:17-jre
COPY --from=build /target/*.jar app.jar
# 暴露端口，Render 会自动分配 PORT 环境变量
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
