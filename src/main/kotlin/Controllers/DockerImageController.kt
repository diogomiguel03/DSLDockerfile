package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.beans.property.SimpleStringProperty
import org.example.Config
import org.example.DSL.DockerImageDSL
import org.example.DSL.Metadata
import org.example.DSL.MetadataDSL
import org.example.DSL.dockerImage
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread

class DockerImageController {
    val dockerfilePath = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")
    val outputLog = SimpleStringProperty("")
    private val objectMapper = jacksonObjectMapper()

    fun createDockerImage(onComplete: (Boolean) -> Unit) {
        val dockerfilePathValue = dockerfilePath.get()
        val imageNameValue = imageName.get()
        val imageTagValue = imageTag.get()

        if (dockerfilePathValue.isNullOrEmpty() || imageNameValue.isNullOrEmpty()) {
            outputLog.set("Dockerfile path and image name must not be null or empty.")
            onComplete(false)
            return
        }

        val dsl = dockerImage {
            from(dockerfilePathValue)
            name(imageNameValue)
            tag(imageTagValue)
        }

        buildAndPushImage(dsl, onComplete)
    }

    private fun buildAndPushImage(dsl: DockerImageDSL, onComplete: (Boolean) -> Unit) {
        thread {
            try {
                val dockerfilePathValue = dsl.dockerfilePath.get()
                val imageNameValue = dsl.imageName.get()
                val imageTagValue = if (dsl.imageTag.get().isNotBlank()) dsl.imageTag.get() else Config.defaultTag
                val fullImageName = "$imageNameValue:$imageTagValue"
                val dockerfile = File(dockerfilePathValue)
                if (!dockerfile.exists()) {
                    val message = "Dockerfile does not exist at: $dockerfilePathValue"
                    outputLog.set(message)
                    onComplete(false)
                    return@thread
                }

                val dockerDirectory = dockerfile.parentFile
                if (dockerDirectory == null || !dockerDirectory.exists()) {
                    val message = "Docker directory does not exist: ${dockerDirectory?.absolutePath}"
                    outputLog.set(message)
                    onComplete(false)
                    return@thread
                }

                val processBuilder = ProcessBuilder(
                    "docker", "build", "-t", fullImageName, "-f", dockerfilePathValue, dockerDirectory.absolutePath
                )
                outputLog.set("Building Docker image with command: ${processBuilder.command().joinToString(" ")}")
                processBuilder.directory(dockerDirectory)
                processBuilder.redirectErrorStream(true)
                val process = processBuilder.start()

                val reader = BufferedReader(InputStreamReader(process.inputStream))
                reader.lines().forEach { outputLog.set(it) }

                val exitCode = process.waitFor()
                if (exitCode == 0) {
                    val message = "Docker image created successfully with name: $fullImageName"
                    outputLog.set(message)
                    updateMetadataFile(dockerfilePathValue, fullImageName)
                    onComplete(true)
                } else {
                    val message = "Failed to create Docker image. Exit code: $exitCode"
                    outputLog.set(message)
                    onComplete(false)
                }
            } catch (e: Exception) {
                val message = "An error occurred while creating the Docker image: ${e.message}"
                outputLog.set(message)
                onComplete(false)
            }
        }
    }

    private fun updateMetadataFile(dockerfilePath: String, imageName: String) {
        try {
            val metadataFile = File(File(dockerfilePath).parent, "metadata-info.json")
            if (metadataFile.exists()) {
                val metadata: Metadata = MetadataDSL.parseFromFile(metadataFile)
                val updatedMetadata = metadata.copy(imageNames = (metadata.imageNames + imageName).toMutableList())
                MetadataDSL.saveToFile(metadataFile.absolutePath, updatedMetadata)
                outputLog.set("Metadata file updated: ${metadataFile.absolutePath}")
            } else {
                outputLog.set("Metadata file does not exist: ${metadataFile.absolutePath}")
            }
        } catch (e: Exception) {
            outputLog.set("Failed to update metadata file: ${e.message}")
        }
    }
}
