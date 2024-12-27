# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /zenflix

# Add the application's JAR file to the container
COPY target/zenflix-*.jar zenflix-app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "zenflix-app.jar"]
