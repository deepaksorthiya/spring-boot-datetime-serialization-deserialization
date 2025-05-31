# Stage 1: Build Stage
FROM bellsoft/liberica-runtime-container:jdk-24-stream-musl AS builder

WORKDIR /home/app
COPY . /home/app/spring-boot-datetime-serialization-deserialization
WORKDIR /home/app/spring-boot-datetime-serialization-deserialization
RUN  chmod +x mvnw && ./mvnw -Dmaven.test.skip=true clean package

# Stage 2: Layer Tool Stage
FROM bellsoft/liberica-runtime-container:jdk-24-cds-slim-musl AS optimizer

WORKDIR /home/app
COPY --from=builder /home/app/spring-boot-datetime-serialization-deserialization/target/*.jar spring-boot-datetime-serialization-deserialization.jar
RUN java -Djarmode=tools -jar spring-boot-datetime-serialization-deserialization.jar extract --layers --launcher

# Stage 3: Final Stage
FROM bellsoft/liberica-runtime-container:jre-24-stream-musl

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
COPY --from=optimizer /home/app/spring-boot-datetime-serialization-deserialization/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-datetime-serialization-deserialization/spring-boot-loader/ ./
COPY --from=optimizer /home/app/spring-boot-datetime-serialization-deserialization/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-datetime-serialization-deserialization/application/ ./