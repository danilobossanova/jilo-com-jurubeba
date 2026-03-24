# ----------------------------
# 1) BUILD STAGE (Maven)
# ----------------------------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# ----------------------------
# 2) RUNTIME STAGE
# ----------------------------
FROM eclipse-temurin:21-jre-alpine

RUN addgroup --system spring && adduser --system spring --ingroup spring

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
