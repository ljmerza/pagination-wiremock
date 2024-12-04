FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package

FROM wiremock/wiremock:2.35.0

WORKDIR /home/wiremock/extensions

COPY --from=builder /app/target/*.jar .

EXPOSE 8080

CMD ["--verbose"]
