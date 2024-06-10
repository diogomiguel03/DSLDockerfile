// File: DockerfileController.kt
package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import org.example.DSL.InstructionsDockerfile
import java.io.File
import java.time.Instant

data class DockerfileMetadata(val name: String, val timestamp: String, var imageName: String? = null)

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
        createMetadataFile(dockerfileName, filePath)
        println("Dockerfile created at $filePath")
    }

    private fun createMetadataFile(dockerfileName: String, filePath: String) {
        val metadata = DockerfileMetadata(
            name = dockerfileName,
            timestamp = Instant.now().toString()
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
            createMetadataFile(dockerfileName, filePath)
        } else {
            throw IllegalArgumentException("Either parameters or filePath must be provided")
        }
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