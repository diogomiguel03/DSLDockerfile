package org.example.Views

import javafx.scene.layout.VBox
import javafx.scene.text.Text
import org.example.DSL.Metadata
import tornadofx.*

class MetadataEntryPane(metadata: Metadata) : VBox() {
    init {
        spacing = 10.0
        paddingAll = 20.0

        children.addAll(
            Text("Name: ${metadata.name}"),
            Text("Timestamp: ${metadata.timestamp}"),
            Text("Dockerfile Path: ${metadata.dockerfilePath}"),
            Text("Dockerfile Type: ${metadata.dockerfileType ?: "Unknown"}"),
            Text("Image Names: ${metadata.imageNames.joinToString(", ")}"),
            Text("Containers: ${metadata.containers.joinToString(", ")}")
        )
    }
}
