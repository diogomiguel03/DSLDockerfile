package org.example.Generator

import java.io.BufferedReader
import java.io.InputStreamReader

class ContainerManager {
    fun listContainers(all: Boolean = false): List<String> {
        val args = if (all) listOf("docker", "ps", "-a") else listOf("docker", "ps")
        return runCommand(args)
    }

    fun listImages(): List<String> {
        val args = listOf("docker", "images", "--format", "{{.Repository}}:{{.Tag}}")
        return runCommand(args)
    }

    fun runContainer(imageName: String, containerName: String, ports: List<String> = emptyList(), envs: List<String> = emptyList(), volumes: List<String> = emptyList()): Boolean {
        val portArgs = ports.flatMap { listOf("-p", it) }
        val envArgs = envs.flatMap { listOf("-e", it) }
        val volumeArgs = volumes.flatMap { listOf("-v", it) }
        val args = listOf("docker", "run", "--name", containerName) + portArgs + envArgs + volumeArgs + listOf("-d", imageName)
        return runCommand(args).isNotEmpty()
    }

    fun stopContainer(containerName: String): Boolean {
        val args = listOf("docker", "stop", containerName)
        return runCommand(args).isNotEmpty()
    }

    fun removeContainer(containerName: String): Boolean {
        val args = listOf("docker", "rm", containerName)
        return runCommand(args).isNotEmpty()
    }

    private fun runCommand(args: List<String>): List<String> {
        val processBuilder = ProcessBuilder(args)
        val process = processBuilder.start()
        val output = mutableListOf<String>()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            reader.lines().forEach { output.add(it) }
        }
        process.waitFor()
        return output
    }
}
