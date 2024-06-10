// File: MetadataEntryPane.kt
package org.example.Views

import javafx.scene.layout.VBox
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.example.Models.Metadata
import tornadofx.*

class MetadataEntryPane(metadata: Metadata) : VBox() {
    init {
        spacing = 10.0
        paddingAll = 20.0
        style {
            borderWidth += box(1.px)
            borderColor += box(c("#cccccc"))
            borderRadius += box(5.px)
        }

        val dockerfilePathLabel = Text("Dockerfile: ${metadata.dockerfilePath}").apply {
            style {
                fontWeight = FontWeight.BOLD
                fontSize = 14.px
            }
        }

        val nameLabel = Text("Name: ${metadata.name}")
        val timestampLabel = Text("Timestamp: ${metadata.timestamp}")
        val dockerfileTypeLabel = Text("Type: ${metadata.dockerfileType ?: "Unknown"}")
        val imagesLabel = Text("Created Images: ${if (metadata.imageNames.isNotEmpty()) metadata.imageNames.joinToString(", ") else "None"}")
        val containersLabel = Text("Containers: ${if (metadata.containers.isNotEmpty()) metadata.containers.joinToString(", ") else "None"}")

        children.addAll(dockerfilePathLabel, nameLabel, timestampLabel, dockerfileTypeLabel, imagesLabel, containersLabel)
    }
}
