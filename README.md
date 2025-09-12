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

## Troubleshooting
- Ensure Docker is running and you have access to the required images
- For more advanced configuration, see the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image)