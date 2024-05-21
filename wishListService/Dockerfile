FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/wishListService-0.0.1-SNAPSHOT.jar wishlist-service.jar
ENTRYPOINT ["java", "-jar", "wishList-service.jar"]