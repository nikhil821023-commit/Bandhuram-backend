# ---------- Stage 1: Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

# Create the uploads directory and hand ownership to the spring user
# BEFORE switching to that user — this is the fix
RUN mkdir -p /app/uploads && chown -R spring:spring /app/uploads

USER spring:spring

COPY --chown=spring:spring --from=build /app/target/*.jar app.jar

VOLUME /app/uploads

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]