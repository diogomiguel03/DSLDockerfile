// File: MetadataView.kt
package org.example.Views

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.stage.DirectoryChooser
import org.example.MainMenu
import org.example.Models.Metadata
import tornadofx.*
import java.io.File

class MetadataView : View("Metadata Viewer") {
    private val selectedDirectory = SimpleStringProperty()
    private val metadataContent = SimpleStringProperty()

    override val root = vbox {
        spacing = 10.0
        paddingAll = 20.0

        button("Select Directory") {
            action {
                val directoryChooser = DirectoryChooser()
                directoryChooser.title = "Select Directory"
                val selectedDir = directoryChooser.showDialog(null)
                if (selectedDir != null) {
                    selectedDirectory.set(selectedDir.absolutePath)
                    findAndDisplayMetadataFiles(selectedDir)
                } else {
                    alert(Alert.AlertType.WARNING, "Warning", "No directory selected.")
                }
            }
        }

        label("Selected Directory:")
        textfield(selectedDirectory) {
            isEditable = false
            prefWidth = 400.0
        }

        label("Metadata Content:")
        textarea(metadataContent) {
            isEditable = false
            prefRowCount = 15
            prefColumnCount = 50
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }

    private fun findAndDisplayMetadataFiles(directory: File) {
        val metadataFiles = directory.walk()
            .filter { it.isFile && it.name == "metadata-info.json" }
            .toList()

        if (metadataFiles.isEmpty()) {
            metadataContent.set("No metadata files found in the selected directory.")
            return
        }

        val content = StringBuilder()
        for (file in metadataFiles) {
            val metadata = parseMetadataFile(file)
            content.append("Metadata file: ${file.absolutePath}\n")
            content.append("Dockerfile: ${metadata.dockerfilePath}\n")
            content.append("Name: ${metadata.name}\n")
            content.append("Timestamp: ${metadata.timestamp}\n")
            if (metadata.imageName != null) {
                content.append("Created Image: ${metadata.imageName}\n")
            } else {
                content.append("Created Image: Not yet created\n")
            }
            content.append("\n\n")
        }
        metadataContent.set(content.toString())
    }

    private fun parseMetadataFile(file: File): Metadata {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(file, Metadata::class.java)
    }
}
