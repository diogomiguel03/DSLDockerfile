package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import mu.KotlinLogging
import org.example.DSL.DockerfileDSL
import org.example.DSL.MetadataDSL
import tornadofx.*
import java.io.File
import java.time.Instant

private val logger = KotlinLogging.logger {}

class DockerfileController {
    val previewContent = SimpleStringProperty()
    private val objectMapper = jacksonObjectMapper()
    private val dsl = DockerfileDSL()

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
        dsl.apply {
            from(image, tag)
            run(updateCommand)
            run(installCommand)
            workdir(workdirPath)
            copy(copySource, copyDestination)
            run(compileCommand)
            cmd(cmdCommand)
        }
        logger.info { "Generating Dockerfile at $filePath with instructions: ${dsl.build()}" }
        dsl.saveToFile(filePath)

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
            else -> "Unknown"
        }
    }

    private fun createMetadataFile(dockerfileName: String, filePath: String, dockerfileType: String) {
        val metadata = MetadataDSL().apply {
            name = dockerfileName
            timestamp = Instant.now().toString()
            this.dockerfilePath = filePath
            this.dockerfileType = dockerfileType
        }.build()

        val metadataFile = File(File(filePath).parent, "metadata-info.json")
        MetadataDSL.saveToFile(metadataFile.absolutePath, metadata)
    }

    fun createCustomDockerfile(instruction: String, parameters: String? = null, filePath: String? = null) {
        if (parameters != null) {
            // Add instruction
            logger.info { "Adding instruction: $instruction with parameters: $parameters" }
            when (instruction) {
                "FROM" -> {
                    val parts = parameters.split(" ")
                    val image = parts[0]
                    val tag = if (parts.size > 1) parts[1] else "latest"
                    dsl.from(image, tag)
                }
                "ADD" -> {
                    val (source, destination) = parameters.split(" ")
                    dsl.add(source, destination)
                }
                "ARG" -> {
                    val (name, defaultValue) = parameters.split(" ")
                    dsl.arg(name, defaultValue)
                }
                "CMD" -> dsl.cmd(parameters)
                "ENTRYPOINT" -> dsl.entrypoint(parameters)
                "ENV" -> {
                    val (key, value) = parameters.split(" ")
                    dsl.env(key, value)
                }
                "EXPOSE" -> dsl.expose(parameters.toInt())
                "HEALTHCHECK" -> dsl.healthcheck(parameters)
                "LABEL" -> {
                    val (key, value) = parameters.split(" ")
                    dsl.label(key, value)
                }
                "MAINTAINER" -> dsl.maintainer(parameters)
                "RUN" -> dsl.run(parameters)
                "SHELL" -> dsl.shell(parameters)
                "STOPSIGNAL" -> dsl.stopsignal(parameters)
                "USER" -> dsl.user(parameters)
                "VOLUME" -> dsl.volume(parameters)
                "WORKDIR" -> dsl.workdir(parameters)
                "COPY" -> {
                    val (source, destination) = parameters.split(" ")
                    dsl.copy(source, destination)
                }
                else -> throw IllegalArgumentException("Unsupported instruction: $instruction")
            }
            updatePreview()
        } else if (filePath != null) {
            // Generate Dockerfile
            logger.info { "Generating Dockerfile at $filePath with instructions: ${dsl.build()}" }
            dsl.saveToFile(filePath)

            val dockerfileName = File(filePath).nameWithoutExtension
            val dockerfileType = determineDockerfileType(dsl.getFromImage() ?: "Unknown")
            createMetadataFile(dockerfileName, filePath, dockerfileType)
        } else {
            throw IllegalArgumentException("Either parameters or filePath must be provided")
        }
    }

    fun loadDockerfile(file: File, dockerfileContent: SimpleStringProperty) {
        val content = file.readText()
        dockerfileContent.set(content)
    }

    fun saveChanges(filePath: SimpleStringProperty, dockerfileContent: SimpleStringProperty) {
        val path = filePath.get()
        if (path.isNullOrEmpty()) {
            alert(Alert.AlertType.WARNING, "Warning", "Please select a Dockerfile.")
            return
        }

        val content = dockerfileContent.get()
        try {
            File(path).writeText(content)
            alert(Alert.AlertType.INFORMATION, "Success", "Dockerfile saved successfully.")
        } catch (e: Exception) {
            alert(Alert.AlertType.ERROR, "Error", "Failed to save Dockerfile: ${e.message}")
        }
    }

    fun saveDockerfile(filePath: String, content: String) {
        File(filePath).writeText(content)
    }

    private fun updatePreview() {
        val dockerfileContent = dsl.build()
        previewContent.set(dockerfileContent)
    }

}
