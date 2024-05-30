package org.example.Generator

import java.io.BufferedReader
import java.io.InputStreamReader

class ContainerManager {
    fun listContainers(all: Boolean = false): List<String> {
        val args = if (all) listOf("docker", "ps", "-a", "--format", "{{.ID}}") else listOf("docker", "ps", "--format", "{{.ID}}")
        return runCommand(args).second
    }

    fun listImages(): List<String> {
        val args = listOf("docker", "images", "--format", "{{.Repository}}:{{.Tag}}")
        return runCommand(args).second
    }

    fun runContainer(containerName: String, imageName: String? = null, ports: List<String> = emptyList(), envs: List<String> = emptyList(), volumes: List<String> = emptyList()): Boolean {
        val portArgs = ports.flatMap { listOf("-p", it) }
        val envArgs = envs.flatMap { listOf("-e", it) }
        val volumeArgs = volumes.flatMap { listOf("-v", it) }
        val args = listOf("docker", "run", "--name", containerName) + portArgs + envArgs + volumeArgs + (imageName?.let { listOf("-d", it) } ?: emptyList())
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            println("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun stopContainer(containerName: String): Boolean {
        val args = listOf("docker", "stop", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            println("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun removeContainer(containerName: String): Boolean {
        val args = listOf("docker", "rm", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            println("Error output: ${output.joinToString("\n")}")
        }
        return success
    }

    fun startContainer(containerName: String): Boolean {
        val args = listOf("docker", "start", containerName)
        println("Running command: ${args.joinToString(" ")}")
        val (success, output) = runCommand(args)
        if (!success) {
            println("Error output: ${output.joinToString("\n")}")
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
