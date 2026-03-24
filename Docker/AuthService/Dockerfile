FROM eclipse-temurin:25-jdk-noble
WORKDIR /app
COPY AuthService/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]