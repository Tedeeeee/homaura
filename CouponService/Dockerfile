FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/CouponService-0.0.1-SNAPSHOT.jar coupon-service.jar
ENTRYPOINT ["java", "-jar", "coupon-service.jar"]