FROM eclipse-temurin:25-jdk-noble
WORKDIR /app
COPY UserService/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]