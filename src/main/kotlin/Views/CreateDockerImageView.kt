// File: CreateDockerImageView.kt
package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import org.example.Generator.GeneratorDockerImage
import org.example.MainMenu
import tornadofx.*

class CreateDockerImageView : View("Create Docker Image") {
    private val dockerfilePath = SimpleStringProperty()
    private val imageName = SimpleStringProperty()
    private val generator = GeneratorDockerImage()

    override val root = vbox {
        paddingAll = 20
        spacing = 10.0

        label("Create Docker Image") {
            style {
                fontSize = 20.px
                fontWeight = FontWeight.BOLD
            }
        }

        hbox {
            spacing = 10.0
            textfield(dockerfilePath) {
                promptText = "Path to Dockerfile"
                prefWidth = 300.0
            }
            button("Browse") {
                action {
                    val fileChooser = FileChooser()
                    fileChooser.title = "Select Dockerfile"
                    val selectedFile = fileChooser.showOpenDialog(null)
                    if (selectedFile != null) {
                        dockerfilePath.set(selectedFile.absolutePath)
                    }
                }
            }
        }

        textfield(imageName) {
            promptText = "Docker Image Name"
            prefWidth = 300.0
        }

        button("Create Image") {
            action {
                val path = dockerfilePath.get()
                val name = imageName.get()
                if (path.isNullOrEmpty() || name.isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide both Dockerfile path and image name.")
                } else {
                    val success = generator.createDockerImage(path, name)
                    if (success) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Docker image created successfully.")
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed to create Docker image.")
                    }
                }
            }
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }
}
