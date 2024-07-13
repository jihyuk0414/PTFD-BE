FROM openjdk:17-jdk
WORKDIR /app
ARG JAR_FILE=build/Api-gateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml /app
ENTRYPOINT ["java","-jar","app.jar"]