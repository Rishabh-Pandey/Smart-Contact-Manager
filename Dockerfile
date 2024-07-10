# syntax=docker/dockerfile:1

# FROM maven:3.8.3-openjdk-17 AS build
# COPY src /home/app/src
# COPY pom.xml /home/app
# RUN mvn -f /home/app/pom.xml clean package
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "/home/app/target/scm2.0-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-alpine
RUN apk --no-cache add curl maven
ENV DB_HOST=${DB_HOST}
ENV DB_NAME=${DB_NAME}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
EXPOSE 8080
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY src src
RUN mvn package -DskipTests
ENTRYPOINT ["java","-jar","target/scm2.0-0.0.1-SNAPSHOT.jar"]