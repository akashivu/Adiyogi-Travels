# Use Java 21 base image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw* pom.xml ./
COPY .mvn .mvn
COPY src ./src

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/Adiyogi-Travels-0.0.1-SNAPSHOT.jar"]
