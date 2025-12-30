# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
# Parent POM
COPY pom.xml .
# Directories - Module placeholders
RUN mkdir -p discovery-server api-gateway identity-service wallet-service reconcile-service transaction-service common && \
    for dir in discovery-server api-gateway identity-service wallet-service reconcile-service transaction-service common; do \
        echo "<project><modelVersion>4.0.0</modelVersion><artifactId>${dir}</artifactId><groupId>com.zenithpay</groupId><version>0.0.1-SNAPSHOT</version></project>" > ${dir}/pom.xml; \
    done
# Service POM
COPY identity-service/pom.xml ./identity-service/pom.xml
# Source Code
COPY identity-service/src ./identity-service/src
# Build service
RUN mvn clean package -pl identity-service -am -DskipTests
# 2. Run Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Install curl for healthchecks
RUN apk add --no-cache curl
# Create a non-root user for security
RUN addgroup -S zenithgroup && adduser -S zenithuser -G zenithgroup
# Get JAR file from build stage
COPY --from=builder /app/identity-service/target/*.jar app.jar
RUN chown zenithuser:zenithgroup app.jar
USER zenithuser
ENTRYPOINT ["java", "-jar", "app.jar"]