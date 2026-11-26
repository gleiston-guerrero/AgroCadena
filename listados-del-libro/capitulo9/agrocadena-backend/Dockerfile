# Etapa 1: build con Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY pom.xml .
RUN ./mvnw -q -B dependency:go-offline
COPY src ./src
RUN ./mvnw -q -B package -DskipTests

# Etapa 2: runtime minimo
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/target/agrocadena-backend-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
