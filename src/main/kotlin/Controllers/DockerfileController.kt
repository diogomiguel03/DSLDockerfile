package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import mu.KotlinLogging
import org.example.DSL.DockerfileDSL
import org.example.DSL.MetadataDSL
import org.example.DSL.dockerfile
import org.example.DSL.metadata
import tornadofx.*
import java.io.File
import java.time.Instant

private val logger = KotlinLogging.logger {}

class DockerfileController {
    val previewContent = SimpleStringProperty()
    private val objectMapper = jacksonObjectMapper()
    private val instructionList = mutableListOf<Pair<String, String>>()  // Lista para armazenar instruções temporariamente

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
        val dsl = dockerfile {
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
        val metadata = metadata {
            name = dockerfileName
            timestamp = Instant.now().toString()
            this.dockerfilePath = filePath
            this.dockerfileType = dockerfileType
        }

        val metadataFile = File(File(filePath).parent, "metadata-info.json")
        MetadataDSL.saveToFile(metadataFile.absolutePath, metadata)
    }

    fun createCustomDockerfile(instruction: String, parameters: String? = null, filePath: String? = null) {
        if (parameters != null) {
            logger.info { "Adding instruction: $instruction with parameters: $parameters" }
            instructionList.add(instruction to parameters)
            updatePreview()
        } else if (filePath != null) {
            val dsl = dockerfile {
                instructionList.forEach { (instr, params) ->
                    when (instr) {
                        "FROM" -> {
                            val parts = params.split(" ")
                            val image = parts[0]
                            val tag = if (parts.size > 1) parts[1] else "latest"
                            from(image, tag)
                        }
                        "ADD" -> {
                            val (source, destination) = params.split(" ")
                            add(source, destination)
                        }
                        "ARG" -> {
                            val (name, defaultValue) = params.split(" ")
                            arg(name, defaultValue)
                        }
                        "CMD" -> cmd(params)
                        "ENTRYPOINT" -> entrypoint(params)
                        "ENV" -> {
                            val (key, value) = params.split(" ")
                            env(key, value)
                        }
                        "EXPOSE" -> expose(params.toInt())
                        "HEALTHCHECK" -> healthcheck(params)
                        "LABEL" -> {
                            val (key, value) = params.split(" ")
                            label(key, value)
                        }
                        "MAINTAINER" -> maintainer(params)
                        "RUN" -> run(params)
                        "SHELL" -> shell(params)
                        "STOPSIGNAL" -> stopsignal(params)
                        "USER" -> user(params)
                        "VOLUME" -> volume(params)
                        "WORKDIR" -> workdir(params)
                        "COPY" -> {
                            val (source, destination) = params.split(" ")
                            copy(source, destination)
                        }
                        else -> throw IllegalArgumentException("Unsupported instruction: $instr")
                    }
                }
            }
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
        val dsl = dockerfile {
            instructionList.forEach { (instr, params) ->
                when (instr) {
                    "FROM" -> {
                        val parts = params.split(" ")
                        val image = parts[0]
                        val tag = if (parts.size > 1) parts[1] else "latest"
                        from(image, tag)
                    }
                    "ADD" -> {
                        val (source, destination) = params.split(" ")
                        add(source, destination)
                    }
                    "ARG" -> {
                        val (name, defaultValue) = params.split(" ")
                        arg(name, defaultValue)
                    }
                    "CMD" -> cmd(params)
                    "ENTRYPOINT" -> entrypoint(params)
                    "ENV" -> {
                        val (key, value) = params.split(" ")
                        env(key, value)
                    }
                    "EXPOSE" -> expose(params.toInt())
                    "HEALTHCHECK" -> healthcheck(params)
                    "LABEL" -> {
                        val (key, value) = params.split(" ")
                        label(key, value)
                    }
                    "MAINTAINER" -> maintainer(params)
                    "RUN" -> run(params)
                    "SHELL" -> shell(params)
                    "STOPSIGNAL" -> stopsignal(params)
                    "USER" -> user(params)
                    "VOLUME" -> volume(params)
                    "WORKDIR" -> workdir(params)
                    "COPY" -> {
                        val (source, destination) = params.split(" ")
                        copy(source, destination)
                    }
                    else -> throw IllegalArgumentException("Unsupported instruction: $instr")
                }
            }
        }
        previewContent.set(dsl.build())
    }
}
