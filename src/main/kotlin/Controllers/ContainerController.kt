package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.Config
import org.example.DSL.*
import org.example.DSL.container
import org.example.Utils.CommandUtils.runCommand
import java.io.File
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

class ContainerController {

    val outputLog = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")
    val name = SimpleStringProperty("")
    val ports = SimpleStringProperty("")
    val envVars = SimpleStringProperty("")
    val volumes = SimpleStringProperty("")
    val dockerfilePath = SimpleStringProperty("")
    private val objectMapper = jacksonObjectMapper()

    fun listContainers(all: Boolean = false): List<String> {
        val args = if (all) listOf("docker", "ps", "-a", "--format", "{{.ID}} {{.Names}}") else listOf("docker", "ps", "--format", "{{.ID}} {{.Names}}")
        return runCommand(args).second
    }

    fun listImages(): List<String> {
        val args = listOf("docker", "images", "--format", "{{.Repository}}:{{.Tag}}")
        return runCommand(args).second
    }

    fun runContainer(metadataDirectory: String, onComplete: (Boolean) -> Unit) {
        val imageNameValue = imageName.get()
        val nameValue = name.get()
        val portsValue = ports.get()
        val imageTagValue = imageTag.get()
        val envVarsValue = envVars.get()
        val volumesValue = volumes.get()

        if (imageNameValue.isNullOrEmpty() || nameValue.isNullOrEmpty() || portsValue.isNullOrEmpty()) {
            outputLog.set("Image name, container name and ports must not be null or empty.")
            onComplete(false)
            return
        }

        val dsl = container {
            name(nameValue)
            image(imageNameValue)
            tag(imageTagValue)
            ports(portsValue)
            envVars(envVarsValue)
            volumes(volumesValue)
        }

        runContainer(dsl, metadataDirectory, onComplete)
    }

    private fun runContainer(dsl: ContainerDSL, metadataDirectory: String, onComplete: (Boolean) -> Unit) {
        thread {
            try {
                val portArgs = dsl.ports.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-p", it) }
                val envArgs = dsl.envVars.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-e", it) }
                val volumeArgs = dsl.volumes.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-v", it) }
                val imageTagValue = if (dsl.imageTag.get().isNotBlank()) dsl.imageTag.get() else Config.defaultTag
                val fullImageName = "${dsl.imageName.get()}:$imageTagValue"
                val args = listOf("docker", "run", "--name", dsl.name.get()) + portArgs + envArgs + volumeArgs + listOf("-d", fullImageName)

                outputLog.set("Running command: ${args.joinToString(" ")}")
                val (success, output) = runCommand(args)
                if (success) {
                    outputLog.set("Container started successfully: ${output.joinToString("\n")}")
                    updateMetadataWithContainer(fullImageName, metadataDirectory, dsl.name.get())
                    onComplete(true)
                } else {
                    outputLog.set("Error output: ${output.joinToString("\n")}")
                    onComplete(false)
                }
            } catch (e: Exception) {
                outputLog.set("An error occurred while running the container: ${e.message}")
                onComplete(false)
            }
        }
    }

    private fun updateMetadataWithContainer(fullImageName: String, metadataDirectory: String, containerName: String) {
        try {
            val metadataFiles = File(metadataDirectory).walk()
                .filter { it.isFile && it.name == "metadata-info.json" }
                .toList()

            for (metadataFile in metadataFiles) {
                val metadata: Metadata = MetadataDSL.parseFromFile(metadataFile)
                if (metadata.imageNames.contains(fullImageName)) {
                    val updatedMetadata = metadata.copy(containers = (metadata.containers + containerName).toMutableList())
                    MetadataDSL.saveToFile(metadataFile.absolutePath, updatedMetadata)
                    outputLog.set("Metadata file updated with container: ${metadataFile.absolutePath}")
                    return
                }
            }

            // Caso nenhum arquivo de metadados correspondente seja encontrado, cria um novo
            val newMetadata = metadata {
                name = File(metadataDirectory).nameWithoutExtension
                timestamp = java.time.Instant.now().toString()
                this.dockerfilePath = metadataDirectory
                imageName(fullImageName)
                container(containerName)
            }
            MetadataDSL.saveToFile("$metadataDirectory/metadata-info.json", newMetadata)
            outputLog.set("No matching metadata file found for image: $fullImageName. New metadata file created: $metadataDirectory/metadata-info.json")
        } catch (e: Exception) {
            outputLog.set("Failed to update metadata file: ${e.message}")
        }
    }


    fun stopContainer(containerName: String): Boolean {
        val args = listOf("docker", "stop", containerName)
        logger.info { "Stopping container: $containerName" }
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
            logger.error { "Error output: ${output.joinToString("\n")}" }
        }
        return success
    }

    fun removeContainer(containerName: String): Boolean {
        val args = listOf("docker", "rm", containerName)
        logger.info { "Removing container: $containerName" }
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
            logger.error { "Error output: ${output.joinToString("\n")}" }
        }
        return success
    }

    fun startContainer(containerName: String): Boolean {
        val args = listOf("docker", "start", containerName)
        logger.info { "Starting container: $containerName" }
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
            logger.error { "Error output: ${output.joinToString("\n")}" }
        }
        return success
    }

    fun getContainerLogs(containerName: String): List<String> {
        val args = listOf("docker", "logs", containerName)
        return runCommand(args).second
    }
}
