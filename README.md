# DSLDockerfile

This project is a **Domain-Specific Language (DSL)** implemented in Kotlin, designed to simplify the creation, management, and automation of Docker-related tasks, including Dockerfile generation, image management, and container orchestration. It provides a graphical interface for users to interact with Docker functionalities, abstracting away the complexities of manual Dockerfile writing and Docker CLI commands. The project leverages TornadoFX for its GUI and Kotlin's DSL capabilities to make Docker configurations more intuitive for developers.

## Features

- **DSL for Docker Objects Creation**: A Kotlin-based DSL (internal) to generate Docker objects with simple, readable syntax.
- **Container Management**: Interact with Docker containers, list running containers, start, stop, and remove containers.
- **Docker Image Management**: Build Docker images from generated Dockerfiles and manage them.
- **Metadata Viewer**: View metadata of Dockerfiles, images, and containers for better tracking and organization.
- **Graphical Interface**: A user-friendly GUI built using TornadoFX that simplifies the process of managing Docker functionalities without needing to write raw Docker commands.

## Project Structure

The project follows an MVC architecture, leveraging Kotlin's internal DSL for Docker automation and TornadoFX for the GUI.

### 1. **Main Files**
   - **MainMenu.kt**: This is the entry point for the graphical interface, which offers options such as creating and editing Dockerfiles, managing containers, viewing metadata, and building Docker images.
   - **main.kt**: The main file to start the application. It launches the TornadoFX GUI and handles the initialization of the application.

### 2. **DSL**
   - **ContainerDSL.kt**: Defines the `ContainerDSL` which allows the user to specify details about Docker containers, such as name, image, ports, environment variables, and volumes.
   - **DockerfileDSL.kt**: Contains the `DockerfileDSL` class, which provides a Kotlin DSL to define Dockerfiles. It includes instructions such as `FROM`, `RUN`, `COPY`, `WORKDIR`, etc., making Dockerfile generation easier and more readable.
   - **DockerImageDSL.kt**: Defines the `DockerImageDSL` class for managing Docker image creation by linking Dockerfiles to image metadata such as the image name, tag, and path.
   - **MetadataDSL.kt**: The MetadataDSL allows users to track the association between Dockerfiles, images, and containers by storing data such as creation timestamps, image names, and container associations in a structured format.

### 3. **Controllers**
   - **ContainerController.kt**: This controller handles the logic for managing Docker containers. It communicates with the Docker CLI to list, start, stop, and remove containers, and update metadata.
   - **DockerfileController.kt**: This controller handles the creation of Dockerfiles, both from the DSL and user input from the GUI. It generates Dockerfiles and saves them to the filesystem.
   - **DockerImageController.kt**: Manages the creation and tracking of Docker images, linking Dockerfiles to the Docker image creation process.

### 4. **Views (TornadoFX GUI)**
   - **CreateDockerfileView.kt**: View that allows users to create a Dockerfile by specifying base image, commands, and other Dockerfile instructions through a user-friendly interface.
   - **CreateDockerImageView.kt**: View for creating Docker images from Dockerfiles.
   - **ContainerManagementView.kt**: Allows the user to manage Docker containers, including starting, stopping, and removing containers, as well as listing running containers.
   - **MetadataView.kt**: Displays metadata for Dockerfiles, images, and containers, providing an overview of the system's state.

## Installation and Usage

### Prerequisites
- Docker installed and running on your machine.
- TornadoFX dependencies.

### DSL Usage

The core of the project is a Kotlin DSL that simplifies the creation of Dockerfiles and the management of Docker containers and images. For example, you can define a Dockerfile like this:

```kotlin
val myDockerfile = dockerfile {
    from("ubuntu", "20.04")
    run("apt-get update")
    run("apt-get install -y curl")
    workdir("/app")
    cmd("bash start.sh")
}
```
