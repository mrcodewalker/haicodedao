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
COPY --from=build /app/target/*.war app.war

# Expose the port that the application runs on
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.war"]

# Example: Docker run command with environment variables
# docker run -p 9091:9091 -e SPRING_DATASOURCE_URL=jdbc:mysql://monorail.proxy.rlwy.net:55842/railway \
#                        -e SPRING_DATASOURCE_USERNAME=root \
#                        -e SPRING_DATASOURCE_PASSWORD=viCcTTMFvChjFidsLYaBdRIpEubzzCFC \
#                        codewalker.kma:latest
