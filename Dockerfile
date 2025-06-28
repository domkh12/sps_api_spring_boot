# PHASE 1 - Download & Install JDK

FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ./build/libs/sps-api-1.0.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=stage", "/app/sps-api-1.0.jar"]