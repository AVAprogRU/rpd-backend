FROM openjdk:17-alpine
ARG JAR_FILE=target/rpd-drafter-1.0.0.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]