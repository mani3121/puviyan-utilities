# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . /app
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
ARG PORT
ENV PORT=${PORT}
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
