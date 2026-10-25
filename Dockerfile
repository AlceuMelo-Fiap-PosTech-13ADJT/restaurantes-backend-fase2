# Etapa 1: Build da Aplicacao
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Cache das dependencias do Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compilacao e empacotamento da aplicacao
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime Otimizado e Seguro
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Criacao de usuario nao-root para execucao segura
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia do artefato compilado
COPY --from=builder /app/target/*.jar app.jar

# Variaveis de ambiente padrao para producao
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
