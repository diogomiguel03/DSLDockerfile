// File: DockerImageController.kt
package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import org.example.Models.DockerImage
import java.io.File

class DockerImageController(private val model: DockerImage) {

    val outputLog = SimpleStringProperty()

    fun createDockerImage(): Boolean {
        try {
            val dockerfilePath = model.dockerfilePath.get()
            val imageName = model.imageName.get()
            val imageTag = model.imageTag.get()
            val fullImageName = "$imageName:$imageTag"
            val dockerfile = File(dockerfilePath)
            if (!dockerfile.exists()) {
                outputLog.set("Dockerfile does not exist at: $dockerfilePath")
                return false
            }

            val dockerDirectory = dockerfile.parentFile
            if (dockerDirectory == null || !dockerDirectory.exists()) {
                outputLog.set("Docker directory does not exist: ${dockerDirectory?.absolutePath}")
                return false
            }

            val processBuilder = ProcessBuilder(
                "docker", "build", "-t", fullImageName, "-f", dockerfilePath, dockerDirectory.absolutePath
            )
            processBuilder.directory(dockerDirectory)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = process.inputStream.bufferedReader()
            reader.lines().forEach { outputLog.set(it) }

            val exitCode = process.waitFor()
            if (exitCode == 0) {
                outputLog.set("Docker image created successfully with name: $fullImageName")
                return true
            } else {
                outputLog.set("Failed to create Docker image. Exit code: $exitCode")
                return false
            }
        } catch (e: Exception) {
            outputLog.set("An error occurred while creating the Docker image: ${e.message}")
            return false
        }
    }
}
