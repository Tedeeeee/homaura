FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/apigatewayServer-0.0.1-SNAPSHOT.jar apigateway-service.jar
ENTRYPOINT ["java", "-jar", "apigateway-service.jar"]