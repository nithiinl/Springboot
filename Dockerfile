# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/webhub1-0.0.1-SNAPSHOT.jar /app/webhub1.jar

# Expose the port that the application uses
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "webhub1.jar"]
