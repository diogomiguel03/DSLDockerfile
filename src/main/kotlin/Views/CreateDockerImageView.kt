package org.example.Views

import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import org.example.Controllers.DockerImageController
import org.example.MainMenu
import tornadofx.*

class CreateDockerImageView : View("Create Docker Image") {
    private val controller = DockerImageController()

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
            textfield(controller.dockerfilePath) {
                promptText = "Path to Dockerfile"
                prefWidth = 300.0
            }
            button("Browse") {
                action {
                    val fileChooser = FileChooser()
                    fileChooser.title = "Select Dockerfile"
                    val selectedFile = fileChooser.showOpenDialog(null)
                    if (selectedFile != null) {
                        controller.dockerfilePath.set(selectedFile.absolutePath)
                    }
                }
            }
        }

        textfield(controller.imageName) {
            promptText = "Docker Image Name"
            prefWidth = 300.0
        }

        textfield(controller.imageTag) {
            promptText = "Docker Image Tag (default: latest)"
            prefWidth = 300.0
        }

        button("Create Image") {
            action {
                if (controller.dockerfilePath.get().isNullOrEmpty() || controller.imageName.get().isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide both Dockerfile path and image name.")
                } else {
                    controller.createDockerImage { success ->
                        runLater {
                            if (success) {
                                alert(Alert.AlertType.INFORMATION, "Success", "Docker image created successfully.")
                            } else {
                                alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
                            }
                        }
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
