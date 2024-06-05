// File: Container.kt
package org.example.Models

import javafx.beans.property.SimpleStringProperty

class Container {
    val name = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest") // default tag is "latest"
    val ports = SimpleStringProperty("")
    val envVars = SimpleStringProperty("")
    val volumes = SimpleStringProperty("")
}
