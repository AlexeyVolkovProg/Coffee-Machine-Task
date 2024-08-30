FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY target/coffee-machine-core-0.0.1-SNAPSHOT.jar /app/coffee-machine-core-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "coffee-machine-core-0.0.1-SNAPSHOT.jar"]
EXPOSE 8090