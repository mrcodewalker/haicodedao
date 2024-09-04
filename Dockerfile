# Start with a base image containing Maven and OpenJDK
FROM maven:3-openjdk-17 AS build
WORKDIR /app

# Copy the Maven project and build it
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller base image for runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY SYSTEM32.env /app/

# Expose the port that the application runs on
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
