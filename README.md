# DSLDockerfile

This project provides a Domain-Specific Language (DSL) in Kotlin to define and generate Dockerfiles and Docker-related objects. The goal is to simplify the creation of the needed objects necessary to deploy a project by allowing users to focus on the logic rather than the syntax.

## Features

- **DSL for Dockerfiles**: Allows defining Dockerfiles in Kotlin using a simple, readable syntax.
- **Command Execution**: Supports managing Docker containers, files and images, including listing, starting, stopping, and removing containers.
- **Graphical User Interface**: A TornadoFX-based UI (in progress) for easier interaction with the DSL and Docker management.

## Project Structure

- **`src/main/kotlin`**: Contains the core Kotlin code for the DSL and UI.
  - **`DSL`**: Houses the Kotlin DSL for the several types of Docker objects.
  - **`Controllers`**: Contains the logic for handling Docker commands (e.g., running containers, building images).
  - **`Views`**: UI implementation using TornadoFX for managing containers and Dockerfiles.
