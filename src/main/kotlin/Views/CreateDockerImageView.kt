// File: CreateDockerImageView.kt
package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import org.example.Controllers.DockerImageController
import org.example.Models.DockerImage
import org.example.MainMenu
import tornadofx.*

class CreateDockerImageView : View("Create Docker Image") {
    private val model = DockerImage()
    private val controller = DockerImageController(model)

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
            textfield(model.dockerfilePath) {
                promptText = "Path to Dockerfile"
                prefWidth = 300.0
            }
            button("Browse") {
                action {
                    val fileChooser = FileChooser()
                    fileChooser.title = "Select Dockerfile"
                    val selectedFile = fileChooser.showOpenDialog(null)
                    if (selectedFile != null) {
                        model.dockerfilePath.set(selectedFile.absolutePath)
                    }
                }
            }
        }

        textfield(model.imageName) {
            promptText = "Docker Image Name"
            prefWidth = 300.0
        }

        textfield(model.imageTag) {
            promptText = "Docker Image Tag (default: latest)"
            prefWidth = 300.0
        }

        button("Create Image") {
            action {
                if (model.dockerfilePath.get().isNullOrEmpty() || model.imageName.get().isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide both Dockerfile path and image name.")
                } else {
                    val success = controller.createDockerImage()
                    if (success) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Docker image created successfully.")
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
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
