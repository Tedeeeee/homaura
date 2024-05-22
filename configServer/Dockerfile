FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/ConfigServer-0.0.1-SNAPSHOT.jar config-service.jar
ENTRYPOINT ["java", "-jar", "config-service.jar"]