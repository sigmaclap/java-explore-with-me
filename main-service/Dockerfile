FROM amazoncorretto:11-alpine-jdk
COPY target/main-service-0.0.1-SNAPSHOT.jar main-service.jar
ENTRYPOINT ["java","-jar","/main-service.jar"]