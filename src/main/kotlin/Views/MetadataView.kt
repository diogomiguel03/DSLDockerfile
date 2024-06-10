// File: MetadataView.kt
package org.example.Views

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import org.example.MainMenu
import org.example.Models.Metadata
import tornadofx.*
import java.io.File

class MetadataView : View("Metadata Viewer") {
    private val selectedDirectory = SimpleStringProperty()
    private val metadataContent = VBox().apply {
        spacing = 10.0
        paddingAll = 20.0
    }

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
        scrollpane {
            content = metadataContent
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
            metadataContent.clear()
            metadataContent.add(Text("No metadata files found in the selected directory."))
            return
        }

        metadataContent.clear()
        for (file in metadataFiles) {
            val metadata = parseMetadataFile(file)
            val metadataPane = MetadataEntryPane(metadata)
            metadataContent.add(metadataPane)
        }
    }

    private fun parseMetadataFile(file: File): Metadata {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(file, Metadata::class.java)
    }
}
