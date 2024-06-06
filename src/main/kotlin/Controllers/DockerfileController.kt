// File: DockerfileController.kt
package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.DSL.InstructionsDockerfile
import java.io.File

private val logger = KotlinLogging.logger {}

class DockerfileController {
    private var instructions = InstructionsDockerfile()
    val previewContent = SimpleStringProperty()

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
        try {
            instructions.from(image, tag)
            instructions.run(updateCommand)
            instructions.run(installCommand)
            instructions.workdir(workdirPath)
            instructions.copy(copySource, copyDestination)
            instructions.run(compileCommand)
            instructions.cmd(cmdCommand)
            generate(filePath)
            logger.info { "Dockerfile created at $filePath" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to create Dockerfile: ${e.message}" }
        }
    }

    fun createCustomDockerfile(instruction: String, parameters: String? = null, filePath: String? = null) {
        try {
            if (parameters != null) {
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
                val content = instructions.build()
                File(filePath).writeText(content)
            } else {
                throw IllegalArgumentException("Either parameters or filePath must be provided")
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to create custom Dockerfile: ${e.message}" }
        }
    }

    private fun generate(filePath: String) {
        try {
            val content = instructions.build()
            File(filePath).writeText(content)
        } catch (e: Exception) {
            logger.error(e) { "Failed to generate Dockerfile: ${e.message}" }
        }
    }

    private fun updatePreview() {
        try {
            val dockerfileContent = instructions.build()
            previewContent.set(dockerfileContent)
        } catch (e: Exception) {
            logger.error(e) { "Failed to update Dockerfile preview: ${e.message}" }
        }
    }

    fun resetInstructions() {
        instructions = InstructionsDockerfile()
    }
}
