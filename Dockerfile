# Stage 1: Build the JAR
FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Copy the JAR to the WireMock image
FROM wiremock/wiremock:latest
COPY --from=builder /app/target/your-transformer.jar /var/wiremock/extensions/your-transformer.jar
ENTRYPOINT ["/docker-entrypoint.sh", "--global-response-templating", "--disable-gzip", "--verbose", "--extensions", "PaginationTransformer"]