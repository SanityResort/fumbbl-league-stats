# Fumbbl League Stats

## Building the Docker Image

This project uses the Spring Boot Maven Plugin with Cloud Native Buildpacks to build a Docker image.

### Prerequisites
- Java 8 or higher
- Maven 3.2+
- Docker (running locally)

### Build the Image
Run the following command from the project root:

```
mvn spring-boot:build-image
```

This will produce a Docker image named `fumbbl-league-stats:latest`.

## Publishing the Docker Image

You can publish the image to a remote registry by providing registry credentials as Maven properties.

### Command
```
mvn spring-boot:build-image \
  -Ddocker.publishRegistry=your.registry.url \
  -Ddocker.publishUsername=your-username \
  -Ddocker.publishPassword=your-password
```

- Replace `your.registry.url` with your Docker registry (e.g., `docker.io/yourrepo` or `ghcr.io/yourorg`).
- Replace `your-username` and `your-password` with your registry credentials.

### Notes
- You can also set these properties as environment variables or in your Maven settings for automation and security.
- The image will be pushed to the specified registry after building.

## Example: Build and Publish to Docker Hub
```
mvn spring-boot:build-image \
  -Ddocker.publishRegistry=docker.io/yourusername \
  -Ddocker.publishUsername=yourusername \
  -Ddocker.publishPassword=yourpassword
```

## Troubleshooting
- Ensure Docker is running and you have access to the registry.
- For more advanced configuration, see the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image).

