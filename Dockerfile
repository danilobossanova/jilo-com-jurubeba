# ==========================================
# Dockerfile Multi-stage para jilo-com-jurubeba
# ==========================================
# Build otimizado com camadas de cache
# @author Danilo Fernando

# -----------------------------------------
# STAGE 1: Build da aplicação
# -----------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar arquivos Maven (cache de dependências)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Dar permissão de execução ao mvnw
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build da aplicação (skip tests no build - rodar separadamente)
RUN ./mvnw clean package -DskipTests -B

# Extrair layers do JAR para build mais eficiente
RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# -----------------------------------------
# STAGE 2: Runtime da aplicação
# -----------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

# Labels para metadata
LABEL maintainer="Danilo Fernando"
LABEL version="1.0.0"
LABEL description="Sistema de Gestão de Restaurantes - Postech Fase 2"

# Criar usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copiar layers em ordem de frequência de mudança (cache optimization)
COPY --from=builder /app/target/extracted/dependencies/ ./
COPY --from=builder /app/target/extracted/spring-boot-loader/ ./
COPY --from=builder /app/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/target/extracted/application/ ./

# Mudar para usuário não-root
USER appuser

# Porta da aplicação
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicialização com configurações de produção
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "org.springframework.boot.loader.launch.JarLauncher"]
