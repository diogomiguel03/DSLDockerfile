// File: ContainerController.kt
package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import org.example.Models.Container
import java.io.BufferedReader
import java.io.InputStreamReader

class ContainerController {

    val outputLog = SimpleStringProperty()

    fun listContainers(all: Boolean = false): List<String> {
        val args = if (all) listOf("docker", "ps", "-a", "--format", "{{.Names}}") else listOf("docker", "ps", "--format", "{{.Names}}")
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
        val fullImageName = "${container.imageName.get()}:${container.imageTag.get()}"
        val args = listOf("docker", "run", "--name", container.name.get()) + portArgs + envArgs + volumeArgs + listOf("-d", fullImageName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun stopContainer(containerName: String): Boolean {
        val args = listOf("docker", "stop", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun removeContainer(containerName: String): Boolean {
        val args = listOf("docker", "rm", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun startContainer(containerName: String): Boolean {
        val args = listOf("docker", "start", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            outputLog.set("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun getContainerLogs(containerName: String): List<String> {
        val args = listOf("docker", "logs", containerName)
        return runCommand(args).second
    }

    private fun runCommand(args: List<String>): Pair<Boolean, List<String>> {
        val processBuilder = ProcessBuilder(args)
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
        return Pair(success, output)
    }
}
