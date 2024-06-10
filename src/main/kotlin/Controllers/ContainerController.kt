// File: ContainerController.kt
package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.Config
import org.example.Models.Container
import org.example.Models.Metadata
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private val logger = KotlinLogging.logger {}

class ContainerController {

    val outputLog = SimpleStringProperty()
    private val objectMapper = jacksonObjectMapper()

    fun listContainers(all: Boolean = false): List<String> {
        val args = if (all) listOf("docker", "ps", "-a", "--format", "{{.ID}} {{.Names}}") else listOf("docker", "ps", "--format", "{{.ID}} {{.Names}}")
        return runCommand(args).second
    }

    fun listImages(): List<String> {
        val args = listOf("docker", "images", "--format", "{{.Repository}}:{{.Tag}}")
        return runCommand(args).second
    }

    fun runContainer(container: Container): Boolean {
        val portArgs = container.ports.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-p", it) }
        val envArgs = container.envVars.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-e", it) }
        val volumeArgs = container.volumes.get().split(",").filter { it.isNotBlank() }.flatMap { listOf("-v", it) }
        val imageTag = if (container.imageTag.get().isNotBlank()) container.imageTag.get() else Config.defaultTag
        val fullImageName = "${container.imageName.get()}:$imageTag"
        val args = listOf("docker", "run", "--name", container.name.get()) + portArgs + envArgs + volumeArgs + listOf("-d", fullImageName)
        logger.info { "Running command: ${args.joinToString(" ")}" }
        val (success, output) = runCommand(args)
        if (success) {
            outputLog.set("Container started successfully: ${output.joinToString("\n")}")
            updateMetadataWithContainer(container, fullImageName)
        } else {
            outputLog.set("Error output: ${output.joinToString("\n")}")
            logger.error { "Error output: ${output.joinToString("\n")}" }
        }
        return success
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

    private fun updateMetadataWithContainer(container: Container, fullImageName: String) {
        try {
            val currentDirectory = File(".")
            val metadataFiles = currentDirectory.walk()
                .filter { it.isFile && it.name == "metadata-info.json" }
                .toList()

            for (metadataFile in metadataFiles) {
                val metadata: Metadata = objectMapper.readValue(metadataFile)
                if (metadata.imageNames.contains(fullImageName)) {
                    metadata.containers.add(container.name.get())
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

    private fun runCommand(args: List<String>): Pair<Boolean, List<String>> {
        val processBuilder = ProcessBuilder(args)
        return try {
            val process = processBuilder.start()
            val output = mutableListOf<String>()
            val errorOutput = mutableListOf<String>()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.lines().forEach { output.add(it) }
            }
            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                reader.lines().forEach { errorOutput.add(it) }
            }
            val exitCode = process.waitFor()
            val success = exitCode == 0
            if (!success) {
                output.addAll(errorOutput)
            }
            Pair(success, output)
        } catch (e: Exception) {
            logger.error(e) { "Command execution failed: ${args.joinToString(" ")}" }
            Pair(false, listOf("Error: ${e.message}"))
        }
    }
}
