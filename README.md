# Fumbbl League Stats

## Building the Docker Image

This project uses the Spring Boot Maven Plugin with Cloud Native Buildpacks to build a Docker image. There are two ways to build the image:

### Prerequisites
- Java 8
- Maven 3.2+
- Docker (running locally)

### Local Build (without publishing)
To build the image locally without publishing credentials:

```
mvn spring-boot:build-image -P build-image
```

This will produce a Docker image named `fumbbl-league-stats:latest`.

### Build and Publish
To build and publish the image to a remote registry:

```
mvn spring-boot:build-image -P publish-image \
  -Ddocker.publishRegistry=your.registry.url \
  -Ddocker.publishUsername=your-username \
  -Ddocker.publishPassword=your-password
```

Replace:
- `your.registry.url` with your Docker registry (e.g., `docker.io/yourrepo` or `ghcr.io/yourorg`)
- `your-username` and `your-password` with your registry credentials

### Notes
- You can set the publishing properties as environment variables or in your Maven settings for automation and security
- The publish profile will both build and push the image to the specified registry

## Example: Build and Publish to Docker Hub
```
mvn spring-boot:build-image -P publish-image \
  -Ddocker.publishRegistry=docker.io/yourusername \
  -Ddocker.publishUsername=yourusername \
  -Ddocker.publishPassword=yourpassword
```

## Troubleshooting
- Ensure Docker is running and you have access to the registry
- For more advanced configuration, see the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)
