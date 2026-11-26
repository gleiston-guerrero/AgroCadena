# backend/Dockerfile -- imagen multi-etapa (base: lst:agro9-docker)
# Etapa 1: build con el Maven Wrapper incluido en el repositorio
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline
COPY src ./src
RUN ./mvnw -q -B package -DskipTests

# Etapa 2: runtime minimo, usuario sin privilegios
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 agrocadena
COPY --from=build /src/target/agrocadena-backend-*.jar app.jar
USER agrocadena
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
