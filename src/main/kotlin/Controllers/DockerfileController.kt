// File: DockerfileController.kt
package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.beans.property.SimpleStringProperty
import org.example.DSL.InstructionsDockerfile
import org.example.Models.Metadata
import java.io.File
import java.time.Instant

class DockerfileController {
    private var instructions = InstructionsDockerfile()
    val previewContent = SimpleStringProperty()
    private val objectMapper = jacksonObjectMapper()

    fun createDefaultDockerfile(
        image: String,
        tag: String,
        updateCommand: String,
        installCommand: String,
        workdirPath: String,
        copySource: String,
        copyDestination: String,
        compileCommand: String,
        cmdCommand: String,
        filePath: String
    ) {
        instructions.from(image, tag)
        instructions.run(updateCommand)
        instructions.run(installCommand)
        instructions.workdir(workdirPath)
        instructions.copy(copySource, copyDestination)
        instructions.run(compileCommand)
        instructions.cmd(cmdCommand)
        generate(filePath)

        val dockerfileName = File(filePath).nameWithoutExtension
        val dockerfileType = determineDockerfileType(image)
        createMetadataFile(dockerfileName, filePath, dockerfileType)
        println("Dockerfile created at $filePath")
    }

    private fun determineDockerfileType(image: String): String {
        return when {
            image.contains("python", ignoreCase = true) -> "Python"
            image.contains("node", ignoreCase = true) -> "Node.js"
            image.contains("openjdk", ignoreCase = true) -> "Java"
            image.contains("golang", ignoreCase = true) -> "Go"
            image.contains("ruby", ignoreCase = true) -> "Ruby"
            image.contains("php", ignoreCase = true) -> "PHP"
            image.contains("nginx", ignoreCase = true) -> "Nginx"
            image.startsWith("python", ignoreCase = true) -> "Python"
            image.startsWith("node", ignoreCase = true) -> "Node.js"
            image.startsWith("openjdk", ignoreCase = true) -> "Java"
            image.startsWith("golang", ignoreCase = true) -> "Go"
            image.startsWith("ruby", ignoreCase = true) -> "Ruby"
            image.startsWith("php", ignoreCase = true) -> "PHP"
            image.startsWith("nginx", ignoreCase = true) -> "Nginx"
            else -> "Unknown"
        }
    }

    private fun createMetadataFile(dockerfileName: String, filePath: String, dockerfileType: String) {
        val metadata = Metadata(
            name = dockerfileName,
            timestamp = Instant.now().toString(),
            dockerfilePath = filePath,
            dockerfileType = dockerfileType
        )
        val metadataFile = File(File(filePath).parent, "metadata-info.json")
        objectMapper.writeValue(metadataFile, metadata)
    }

    fun createCustomDockerfile(instruction: String, parameters: String? = null, filePath: String? = null) {
        if (parameters != null) {
            // Add instruction
            when (instruction) {
                "FROM" -> {
                    val parts = parameters.split(" ")
                    val image = parts[0]
                    val tag = if (parts.size > 1) parts[1] else null
                    instructions.from(image, tag)
                }
                "ADD" -> {
                    val (source, destination) = parameters.split(" ")
                    instructions.add(source, destination)
                }
                "ARG" -> {
                    val (name, defaultValue) = parameters.split(" ")
                    instructions.arg(name, defaultValue)
                }
                "CMD" -> instructions.cmd(parameters)
                "ENTRYPOINT" -> instructions.entrypoint(parameters)
                "ENV" -> {
                    val (key, value) = parameters.split(" ")
                    instructions.env(key, value)
                }
                "EXPOSE" -> instructions.expose(parameters.toInt())
                "HEALTHCHECK" -> instructions.healthcheck(parameters)
                "LABEL" -> {
                    val (key, value) = parameters.split(" ")
                    instructions.label(key, value)
                }
                "MAINTAINER" -> instructions.maintainer(parameters)
                "RUN" -> instructions.run(parameters)
                "SHELL" -> instructions.shell(parameters)
                "STOPSIGNAL" -> instructions.stopsignal(parameters)
                "USER" -> instructions.user(parameters)
                "VOLUME" -> instructions.volume(parameters)
                "WORKDIR" -> instructions.workdir(parameters)
                "COPY" -> {
                    val (source, destination) = parameters.split(" ")
                    instructions.copy(source, destination)
                }
                else -> throw IllegalArgumentException("Unsupported instruction: $instruction")
            }
            updatePreview()
        } else if (filePath != null) {
            // Generate Dockerfile
            val content = instructions.build()
            File(filePath).writeText(content)

            val dockerfileName = File(filePath).nameWithoutExtension
            val dockerfileType = determineDockerfileType(instructions.getFromImage() ?: "Unknown")
            createMetadataFile(dockerfileName, filePath, dockerfileType)
        } else {
            throw IllegalArgumentException("Either parameters or filePath must be provided")
        }
    }

    fun loadDockerfile(file: File): String {
        return file.readText()
    }

    fun saveDockerfile(filePath: String, content: String) {
        File(filePath).writeText(content)
    }

    private fun generate(filePath: String) {
        val content = instructions.build()
        File(filePath).writeText(content)
    }

    // Method to update the preview content
    private fun updatePreview() {
        val dockerfileContent = instructions.build()
        previewContent.set(dockerfileContent)
    }

    fun resetInstructions() {
        instructions = InstructionsDockerfile()
    }
}
