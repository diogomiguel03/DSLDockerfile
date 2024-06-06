// File: DockerImageController.kt
package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.Models.DockerImage
import org.example.Config
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private val logger = KotlinLogging.logger {}

class DockerImageController(private val model: DockerImage) {

    val outputLog = SimpleStringProperty()

    fun createDockerImage(): Boolean {
        try {
            val dockerfilePath = model.dockerfilePath.get()
            val imageName = model.imageName.get()
            val imageTag = if (model.imageTag.get().isNotBlank()) model.imageTag.get() else Config.defaultTag
            val fullImageName = "$imageName:$imageTag"
            val dockerfile = File(dockerfilePath)
            if (!dockerfile.exists()) {
                val message = "Dockerfile does not exist at: $dockerfilePath"
                outputLog.set(message)
                logger.error { message }
                return false
            }

            val dockerDirectory = dockerfile.parentFile
            if (dockerDirectory == null || !dockerDirectory.exists()) {
                val message = "Docker directory does not exist: ${dockerDirectory?.absolutePath}"
                outputLog.set(message)
                logger.error { message }
                return false
            }

            val processBuilder = ProcessBuilder(
                "docker", "build", "-t", fullImageName, "-f", dockerfilePath, dockerDirectory.absolutePath
            )
            logger.info { "Building Docker image with command: ${processBuilder.command().joinToString(" ")}" }
            processBuilder.directory(dockerDirectory)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.lines().forEach { logger.info { it } }

            val exitCode = process.waitFor()
            return if (exitCode == 0) {
                val message = "Docker image created successfully with name: $fullImageName"
                outputLog.set(message)
                logger.info { message }
                true
            } else {
                val message = "Failed to create Docker image. Exit code: $exitCode"
                outputLog.set(message)
                logger.error { message }
                false
            }
        } catch (e: Exception) {
            val message = "An error occurred while creating the Docker image: ${e.message}"
            outputLog.set(message)
            logger.error(e) { message }
            return false
        }
    }
}
