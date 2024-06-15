package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.example.DSL.MetadataDSL
import org.example.Views.MetadataEntryPane
import tornadofx.*
import java.io.File

class MetadataController {
    val selectedDirectory = SimpleStringProperty()
    val metadataContent = VBox().apply {
        spacing = 10.0
        paddingAll = 20.0
    }

    fun findAndDisplayMetadataFiles(directory: File) {
        val metadataFiles = MetadataDSL.findMetadataFiles(directory)

        if (metadataFiles.isEmpty()) {
            metadataContent.clear()
            metadataContent.add(Text("No metadata files found in the selected directory."))
            return
        }

        metadataContent.clear()
        for (file in metadataFiles) {
            val metadata = MetadataDSL.parseFromFile(file)
            val metadataPane = MetadataEntryPane(metadata)
            metadataContent.add(metadataPane)
        }
    }
}
