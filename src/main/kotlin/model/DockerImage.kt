// File: DockerImage.kt
package org.example.Models

import javafx.beans.property.SimpleStringProperty

class DockerImage {
    val dockerfilePath = SimpleStringProperty()
    val imageName = SimpleStringProperty()
    val imageTag = SimpleStringProperty("latest") // default tag is "latest"
}
