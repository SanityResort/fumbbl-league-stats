# Fumbbl League Stats

## Building the Docker Image

This project uses a multi-stage Dockerfile to build and run the application entirely within containers.

### Prerequisites
- Docker (running locally)

### Build the Docker Image

To build the Docker image, run:

    docker build -t fumbbl-league-stats:latest .

This will produce a Docker image named `fumbbl-league-stats:latest`.

### Run the Docker Container

To run the application and expose port 8080:

    docker run -p 8080:8080 fumbbl-league-stats:latest

## Publishing Docker Image to a Custom Registry

To publish your Docker image to a custom registry, follow these steps:

1. **Tag the image for your custom registry:**
   ```sh
   docker tag fumbbl-league-stats:latest <registry-url>/<repository>/fumbbl-league-stats:latest
   ```

2. **Login to your custom registry:**
   ```sh
   docker login <registry-url>
   ```

3. **Push the image to the registry:**
   ```sh
   docker push <registry-url>/<repository>/fumbbl-league-stats:latest
   ```

**Example:**
```sh
docker tag fumbbl-league-stats:latest myregistry.example.com/fls/fumbbl-league-stats:latest
docker login myregistry.example.com
docker push myregistry.example.com/fls/fumbbl-league-stats:latest
```

Replace `<registry-url>` and `<repository>` with your actual values.

## Troubleshooting
- Ensure Docker is running and you have access to the required images
- For more advanced configuration, see the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)