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

# Copy the .env file to the container
# Make sure SYSTEM32.env is in the same directory as your Dockerfile or specify the correct path
COPY SYSTEM32.env /etc/secrets/SYSTEM32.env

# Expose the port that the application runs on
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Example: Docker run command with environment variables
# docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://monorail.proxy.rlwy.net:55842/railway \
#                        -e SPRING_DATASOURCE_USERNAME=root \
#                        -e SPRING_DATASOURCE_PASSWORD=viCcTTMFvChjFidsLYaBdRIpEubzzCFC \
#                        codewalker.kma:latest
