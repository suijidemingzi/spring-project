# 第一阶段：构建阶段
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
# 避开测试用例编译打包
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
# 暴露端口，Render 会自动分配 PORT 环境变量
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
