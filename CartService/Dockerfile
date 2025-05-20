FROM amazoncorretto:21-alpine-jdk
LABEL authors="Jair"

COPY target/CartService-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]