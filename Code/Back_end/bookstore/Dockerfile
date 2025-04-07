FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/*.jar bookstore.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "bookstore.jar"]