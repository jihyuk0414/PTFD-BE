FROM openjdk:17-oracle
ENV HOME_DIR /work
RUN mkdir -p $HOME_DIR
WORKDIR $HOME_DIR
COPY build/Chatting-0.0.1-SNAPSHOT.jar /work/chat.jar
CMD ["java","-jar","chat.jar"]