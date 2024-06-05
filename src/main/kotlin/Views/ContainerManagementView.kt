// File: ContainerManagementView.kt
package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import org.example.Controllers.ContainerController
import org.example.Models.Container
import org.example.MainMenu
import tornadofx.*

class ContainerManagementView : View("Container Management") {
    private val controller = ContainerController()
    private val containerModel = Container()
    private val availableImages = FXCollections.observableArrayList<String>()
    private val selectedImage = SimpleStringProperty()
    private val availableContainers = FXCollections.observableArrayList<String>()
    private val selectedContainer = SimpleStringProperty()

    override val root = vbox {
        paddingAll = 20
        spacing = 10.0
        style {
            fontFamily = "Poppins"
        }

        label("Container Management") {
            style {
                fontSize = 20.px
                fontWeight = FontWeight.BOLD
            }
        }

        hbox {
            spacing = 10.0
            button("List Running Containers") {
                action {
                    val containers = controller.listContainers()
                    alert(Alert.AlertType.INFORMATION, "Running Containers", containers.joinToString("\n"))
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
            button("List All Containers") {
                action {
                    val containers = controller.listContainers(true)
                    alert(Alert.AlertType.INFORMATION, "All Containers", containers.joinToString("\n"))
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
        }

        label("Run New Container") {
            style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }
        }

        textfield(containerModel.name) {
            promptText = "Container Name"
            prefWidth = 300.0
        }

        hbox {
            spacing = 10.0
            combobox(selectedImage, availableImages) {
                promptText = "Select Docker Image"
                prefWidth = 300.0
            }
            button("Refresh Images") {
                action {
                    availableImages.setAll(controller.listImages())
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
        }

        textfield(containerModel.ports) {
            promptText = "Ports (optional, format: hostPort:containerPort,...)"
            prefWidth = 300.0
        }

        textfield(containerModel.envVars) {
            promptText = "Environment Variables (optional, format: KEY=VALUE,...)"
            prefWidth = 300.0
        }

        textfield(containerModel.volumes) {
            promptText = "Volumes (optional, format: hostPath:containerPath,...)"
            prefWidth = 300.0
        }

        button("Run Container") {
            action {
                val imageParts = selectedImage.get().split(":")
                containerModel.imageName.set(imageParts[0])
                containerModel.imageTag.set(if (imageParts.size > 1) imageParts[1] else "latest")
                if (containerModel.name.get().isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide a container name.")
                } else {
                    val success = controller.runContainer(containerModel)
                    if (success) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Container started successfully.")
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
                    }
                }
            }
            style {
                backgroundColor += c("#bde0fe")
                paddingAll = 10
                borderRadius += box(10.px)
            }
        }

        label("Stop, Remove, and Start Container") {
            style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }
        }

        hbox {
            spacing = 10.0
            combobox(selectedContainer, availableContainers) {
                promptText = "Select Container"
                prefWidth = 300.0
            }
            button("Refresh Containers") {
                action {
                    availableContainers.setAll(controller.listContainers(true))
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
        }

        hbox {
            spacing = 10.0
            button("Stop Container") {
                action {
                    val contName = selectedContainer.get()
                    if (contName.isNullOrEmpty()) {
                        alert(Alert.AlertType.WARNING, "Warning", "Please select a container to stop.")
                    } else {
                        val success = controller.stopContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container stopped successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
                        }
                    }
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
            button("Remove Container") {
                action {
                    val contName = selectedContainer.get()
                    if (contName.isNullOrEmpty()) {
                        alert(Alert.AlertType.WARNING, "Warning", "Please select a container to remove.")
                    } else {
                        val success = controller.removeContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container removed successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
                        }
                    }
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
            button("Start Container") {
                action {
                    val contName = selectedContainer.get()
                    if (contName.isNullOrEmpty()) {
                        alert(Alert.AlertType.WARNING, "Warning", "Please select a container to start.")
                    } else {
                        val success = controller.startContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container started successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", controller.outputLog.get())
                        }
                    }
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }

    init {
        availableImages.setAll(controller.listImages())
        availableContainers.setAll(controller.listContainers(true)) // Load all containers initially
    }
}
