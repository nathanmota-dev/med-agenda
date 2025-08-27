# Etapa de build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /workspace

# Cache de dependências
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Código fonte
COPY src ./src

# Copia a pasta pdf para resources
COPY pdf ./src/main/resources/pdf

# Build do JAR
RUN mvn -B -q -DskipTests clean package


# Etapa final (runtime)
FROM amazoncorretto:21-alpine
WORKDIR /app

# Usuário não-root
RUN addgroup -S app && adduser -S app -G app

# Copia o JAR gerado
COPY --from=build /workspace/target/*.jar /app/app.jar

USER app

# Porta padrão
EXPOSE 8080

# JAVA_OPTS pode ser injetado por ENV
ENV SERVER_PORT=8080 \
    JAVA_OPTS=""

# O Spring lerá as variáveis de ambiente injetadas no run
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
