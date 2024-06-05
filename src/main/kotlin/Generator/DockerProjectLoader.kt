package org.example.Generator

import org.example.model.*
import java.io.File
import java.io.InputStreamReader

/*
class DockerProjectLoader {

    fun loadProjectFromFolder(path: String): Project {
        val dockerfiles = mutableListOf<Dockerfile>()
        val dockerImages = queryDockerImages()
        val dockerContainers = queryDockerContainers()

        // Scan the directory for Dockerfiles
        val projectDir = File(path)
        projectDir.walk().forEach {
            if (it.name.equals("Dockerfile", ignoreCase = true)) {
                dockerfiles.add(Dockerfile(it.parentFile.name, it.absolutePath))
            }
        }

        // Associate Dockerfiles with Docker images
        dockerfiles.forEach { dockerfile ->
            dockerImages.filter { image ->
                image.name.contains(dockerfile.name, ignoreCase = true)
            }.forEach { image ->
                image.dockerfile = dockerfile
            }
        }

        // Associate Docker images with Docker containers
        dockerImages.forEach { image ->
            dockerContainers.filter { container ->
                container.name.startsWith(image.name, ignoreCase = true)
            }.forEach { container ->
                container.image = image
            }
        }

        return Project(dockerfiles, dockerImages, dockerContainers)
    }

    private fun queryDockerImages(): MutableList<DockerImage> {
        val images = mutableListOf<DockerImage>()
        val process = Runtime.getRuntime().exec("docker images --format '{{.Repository}}:{{.Tag}}'")
        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                images.add(DockerImage(line))
            }
        }
        return images
    }

    private fun queryDockerContainers(): MutableList<DockerContainer> {
        val containers = mutableListOf<DockerContainer>()
        val process = Runtime.getRuntime().exec("docker ps --format '{{.Names}}:{{.Image}}'")
        process.inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(":")
                if (parts.size == 2) {
                    containers.add(DockerContainer(parts[0], DockerImage(parts[1])))
                }
            }
        }
        return containers
    }
}


 */