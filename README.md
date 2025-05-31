[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-datetime-serialization-deserialization/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-datetime-serialization-deserialization/actions/workflows/maven-build.yml) <br>
[![Docker Pulls](https://img.shields.io/docker/pulls/deepaksorthiya/spring-boot-datetime-serialization-deserialization)](https://hub.docker.com/r/deepaksorthiya/spring-boot-datetime-serialization-deserialization)

---

### ** Spring Boot Date and Time Format, Serialization and DeSerialization Testing **

---

# Getting Started

## Requirements:

```
Git: 2.49.0
Spring Boot: 3.5.0
Maven: 3.9+
Java: 24
Docker Desktop: Tested on 4.41.0
```

## Clone this repository:

```bash
git clone https://github.com/deepaksorthiya/spring-boot-datetime-serialization-deserialization.git
cd spring-boot-datetime-serialization-deserialization
```

## Build Project:

```bash
./mvnw clean package -DskipTests
```

## Run Project:

```bash
./mvnw spring-boot:run
```

OR

```bash
java -jar .\target\spring-boot-datetime-serialization-deserialization-0.0.1-SNAPSHOT.jar
```

## (Optional)Build Docker Image(docker should be running):

```bash
./mvnw clean spring-boot:build-image -DskipTests
```

## (Optional)Running On Docker

```bash
docker run -p 8080:8080 --name spring-boot-datetime-serialization-deserialization deepaksorthiya/spring-boot-datetime-serialization-deserialization:0.0.1-SNAPSHOT
```

## Run on Local minikube Kubernetes using Helm Chart

```bash
cd helm
helm create spring-boot-datetime-serialization-deserialization
helm lint spring-boot-datetime-serialization-deserialization
helm install spring-boot-datetime-serialization-deserialization spring-boot-datetime-serialization-deserialization
helm uninstallspring-boot-datetime-serialization-deserialization spring-boot-datetime-serialization-deserialization
```

## Testing

[Postman API Collection](https://www.postman.com/deepaksorthiya/workspace/public-ws/collection/12463530-67a8dfe6-71e4-4963-a31d-b725c1942f41?action=share&creator=12463530&active-environment=12463530-55c10ebe-548f-4c1b-a5ec-4d4ed996c033)

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot//io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)


