package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.DSL.container
import org.example.Utils.CommandUtils.runCommand

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
        val ports = ports.get()
        val imageTagValue = imageTag.get()
        val envVars = envVars.get()
        val volumes = volumes.get()

        if (imageNameValue.isNullOrEmpty() || nameValue.isNullOrEmpty() || ports.isNullOrEmpty()) {
            outputLog.set("Image name, container name and ports must not be null or empty.")
            onComplete(false)
            return
        }

        container({
            name(nameValue)
            image(imageNameValue)
            tag(imageTagValue)
            ports(ports)
            envVars(envVars)
            volumes(volumes)
        }, metadataDirectory, onComplete)
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
