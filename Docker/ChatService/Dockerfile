FROM eclipse-temurin:25-jdk-noble
WORKDIR /app
COPY ChatService/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]