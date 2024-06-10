// File: DockerImageController.kt
package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import org.example.Models.DockerImage
import org.example.Models.Metadata
import org.example.Config
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class DockerImageController(private val model: DockerImage) {

    val outputLog = SimpleStringProperty()
    private val objectMapper = jacksonObjectMapper()

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
                return false
            }

            val dockerDirectory = dockerfile.parentFile
            if (dockerDirectory == null || !dockerDirectory.exists()) {
                val message = "Docker directory does not exist: ${dockerDirectory?.absolutePath}"
                outputLog.set(message)
                return false
            }

            val processBuilder = ProcessBuilder(
                "docker", "build", "-t", fullImageName, "-f", dockerfilePath, dockerDirectory.absolutePath
            )
            outputLog.set("Building Docker image with command: ${processBuilder.command().joinToString(" ")}")
            processBuilder.directory(dockerDirectory)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            reader.lines().forEach { output.append(it).append("\n") }

            val exitCode = process.waitFor()
            outputLog.set(output.toString())
            return if (exitCode == 0) {
                val message = "Docker image created successfully with name: $fullImageName"
                outputLog.set(message)
                updateMetadataFile(dockerfilePath, fullImageName)
                true
            } else {
                val message = "Failed to create Docker image. Exit code: $exitCode\n$output"
                outputLog.set(message)
                return false
            }
        } catch (e: Exception) {
            val message = "An error occurred while creating the Docker image: ${e.message}"
            outputLog.set(message)
            return false
        }
    }

    private fun updateMetadataFile(dockerfilePath: String, imageName: String) {
        try {
            val metadataFile = File(File(dockerfilePath).parent, "metadata-info.json")
            if (metadataFile.exists()) {
                val metadata: Metadata = objectMapper.readValue(metadataFile)
                metadata.imageName = imageName
                objectMapper.writeValue(metadataFile, metadata)
                outputLog.set("Metadata file updated: ${metadataFile.absolutePath}")
            } else {
                outputLog.set("Metadata file does not exist: ${metadataFile.absolutePath}")
            }
        } catch (e: Exception) {
            outputLog.set("Failed to update metadata file: ${e.message}")
        }
    }
}
