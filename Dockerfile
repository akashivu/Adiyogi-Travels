FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY mvnw* pom.xml ./
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 10000

CMD ["java", "-jar", "target/Adiyogi-Travels-0.0.1-SNAPSHOT.jar"]