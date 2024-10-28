# Etapa de build
FROM maven:3.8.8-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -X -DskipTests

# Etapa final
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/finalpoo2.jar
ENTRYPOINT ["java", "-jar", "/app/finalpoo2.jar"]
