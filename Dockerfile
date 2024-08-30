FROM maven:3.9.9-amazoncorretto-17-al2023 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -pl coffee-machine-api -am

RUN mvn clean install -pl coffee-machine-core -am

FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY --from=build /app/coffee-machine-core/target/coffee-machine-core-0.0.1-SNAPSHOT.jar /app/coffee-machine-core-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "coffee-machine-core-0.0.1-SNAPSHOT.jar"]

EXPOSE 8090
