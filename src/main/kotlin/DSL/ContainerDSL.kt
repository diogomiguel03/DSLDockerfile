package org.example.DSL

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import org.example.Config
import org.example.Models.Metadata
import org.example.Utils.CommandUtils.runCommand
import java.io.File
import kotlin.concurrent.thread

class ContainerDSL {
    val name = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")
    val ports = SimpleStringProperty("")
    val envVars = SimpleStringProperty("")
    val volumes = SimpleStringProperty("")
    val dockerfilePath = SimpleStringProperty("")
    val outputLog = SimpleStringProperty("")
    private val objectMapper = jacksonObjectMapper()

    fun fromDockerfile(path: String) {
        dockerfilePath.set(path)
    }

    fun name(containerName: String) {
        name.set(containerName)
    }

    fun image(image: String) {
        imageName.set(image)
    }

    fun tag(tag: String) {
        imageTag.set(tag)
    }

    fun ports(ports: String) {
        this.ports.set(ports)
    }

    fun envVars(envVars: String) {
        this.envVars.set(envVars)
    }

    fun volumes(volumes: String) {
        this.volumes.set(volumes)
    }

    fun run(metadataDirectory: String, onComplete: (Boolean) -> Unit) {
        thread {
            try {
                val portArgs = ports.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-p", it) }
                val envArgs = envVars.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-e", it) }
                val volumeArgs = volumes.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-v", it) }
                val imageTagValue = if (imageTag.get().isNotBlank()) imageTag.get() else Config.defaultTag
                val fullImageName = "${imageName.get()}:$imageTagValue"
                val args = listOf("docker", "run", "--name", name.get()) + portArgs + envArgs + volumeArgs + listOf("-d", fullImageName)

                outputLog.set("Running command: ${args.joinToString(" ")}")
                val (success, output) = runCommand(args)
                if (success) {
                    outputLog.set("Container started successfully: ${output.joinToString("\n")}")
                    updateMetadataWithContainer(fullImageName, metadataDirectory)
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

    private fun updateMetadataWithContainer(fullImageName: String, metadataDirectory: String) {
        try {
            val metadataFiles = File(metadataDirectory).walk()
                .filter { it.isFile && it.name == "metadata-info.json" }
                .toList()

            for (metadataFile in metadataFiles) {
                val metadata: Metadata = objectMapper.readValue(metadataFile)
                if (metadata.imageNames.contains(fullImageName)) {
                    metadata.containers.add(name.get())
                    objectMapper.writeValue(metadataFile, metadata)
                    outputLog.set("Metadata file updated with container: ${metadataFile.absolutePath}")
                    return
                }
            }
            outputLog.set("No matching metadata file found for image: $fullImageName")
        } catch (e: Exception) {
            outputLog.set("Failed to update metadata file: ${e.message}")
        }
    }
}

fun container(init: ContainerDSL.() -> Unit, metadataDirectory: String, onComplete: (Boolean) -> Unit) {
    val dsl = ContainerDSL()
    dsl.init()
    dsl.run(metadataDirectory, onComplete)
}
