FROM openjdk:17-ea-11jdk-slim
VOLUME /tmp
COPY ./apiEncryptionKey.jks apiEncryptionKey.jks
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]