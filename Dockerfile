# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Set Maven options directly in the build command
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN"

# Copy only the files needed for dependency resolution first
COPY pom.xml .

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the application with all necessary flags
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B package \
    -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.site.skip=true \
    -Dmaven.gitcommitid.skip=true

# Run stage
FROM eclipse-temurin:21-jre-jammy AS runtime

# Update package lists and install required packages
RUN apt-get update && \
    # Install required packages
    apt-get install -y --no-install-recommends tzdata && \
    # Create app user and group with fixed UID/GID
    groupadd --system --gid 10001 appuser && \
    useradd --system --uid 10001 --gid appuser --shell /bin/false appuser && \
    mkdir -p /app && \
    chown -R appuser:appuser /app && \
    # Set timezone
    ln -snf /usr/share/zoneinfo/America/Bogota /etc/localtime && \
    echo "America/Bogota" > /etc/timezone && \
    # Clean up
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/apt/archives/*.deb \
           /var/cache/apt/archives/partial/*.deb /var/cache/apt/*.bin || true

# Set environment variables
ENV TZ=America/Bogota \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_HOME=/opt/java/openjdk \
    APP_HOME=/app

WORKDIR ${APP_HOME}

# Copy the JAR file from the build stage
COPY --from=build --chown=appuser:appuser /app/target/*.jar app.jar

# Set file permissions
RUN chmod -R u=rwX,g=rX,o= ${APP_HOME} && \
    chmod -R g=u ${APP_HOME} /etc/passwd

# Switch to non-root user
USER 10001

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=2 -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom"

# Expose the port the app runs on
EXPOSE 8080

# Health check with fixed command
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD ["sh", "-c", "wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1"]

# Run the application with exec form to avoid shell injection
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar"]
