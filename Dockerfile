# syntax=docker/dockerfile:1

# ---------- Etapa 1: Build ----------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia primero el wrapper y el pom para aprovechar la caché de capas de Docker.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Código fuente y empaquetado del jar ejecutable (sin tests para acelerar el deploy).
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# ---------- Etapa 2: Run ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia únicamente el jar repackaged de Spring Boot generado en la etapa de build.
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
