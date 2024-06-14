// File: MetadataController.kt
package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.example.Models.Metadata
import org.example.Views.MetadataEntryPane
import tornadofx.*
import java.io.File

class MetadataController {
    private val objectMapper = jacksonObjectMapper()
    val selectedDirectory = SimpleStringProperty()
    val metadataContent = VBox().apply {
        spacing = 10.0
        paddingAll = 20.0
    }

    fun findAndDisplayMetadataFiles(directory: File) {
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
        return objectMapper.readValue(file, Metadata::class.java)
    }
}
