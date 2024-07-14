# FROM openjdk:17-oracle
# WORKDIR /app
# ARG JAR_FILE=build/Purchase-0.0.1-SNAPSHOT.jar
# COPY ${JAR_FILE} app.jar
# COPY src/main/resources/application.yml /app
# ENTRYPOINT ["java","-jar","app.jar"]

FROM openjdk:17-oracle
ENV HOME_DIR /work
RUN mkdir -p $HOME_DIR
WORKDIR $HOME_DIR
COPY build/Purchase-0.0.1-SNAPSHOT.jar /work/purchase.jar
CMD ["java","-jar","purchase.jar"]