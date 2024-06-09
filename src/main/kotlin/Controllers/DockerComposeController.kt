// File: DockerComposeController.kt
package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import mu.KotlinLogging
import org.example.DSL.DockerComposeFile
import org.example.DSL.DockerComposeService
import java.io.File

private val logger = KotlinLogging.logger {}

class DockerComposeController {
    private val composeFile = DockerComposeFile()
    val previewContent = SimpleStringProperty()

    fun addService(service: DockerComposeService) {
        composeFile.addService(service)
        updatePreview()
    }

    fun generate(filePath: String) {
        try {
            val content = composeFile.build()
            File(filePath).writeText(content)
            logger.info { "Docker Compose file created at $filePath" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to generate Docker Compose file: ${e.message}" }
        }
    }

    private fun updatePreview() {
        try {
            val composeContent = composeFile.build()
            previewContent.set(composeContent)
        } catch (e: Exception) {
            logger.error(e) { "Failed to update Docker Compose preview: ${e.message}" }
        }
    }

    fun resetComposeFile() {
        composeFile.services.clear()
        updatePreview()
    }
}
