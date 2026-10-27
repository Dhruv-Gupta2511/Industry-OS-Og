# Build Stage
FROM maven:3.9.15-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/industry-os-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
