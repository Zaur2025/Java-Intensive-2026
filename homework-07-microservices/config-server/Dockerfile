FROM eclipse-temurin:17-jdk-jammy
COPY target/*.jar app.jar
COPY config-repo /config-repo
ENTRYPOINT ["java", "-jar", "app.jar"]