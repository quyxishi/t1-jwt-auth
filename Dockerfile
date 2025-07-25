FROM gradle:8.10-jdk21 AS builder
WORKDIR /app

RUN apt-get update && \
    apt-get install -y openssl && \
    rm -rf /var/lib/apt/lists/*

COPY . .
RUN mkdir -p ./src/main/resources/keys && \
    openssl ecparam -genkey -name secp521r1 -noout -out ./src/main/resources/keys/ecdsa-p521-private.pem && \
    openssl ec -in ./src/main/resources/keys/ecdsa-p521-private.pem -pubout -out ./src/main/resources/keys/ecdsa-p521-public.pem

RUN ./gradlew clean build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]