package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.ComboBox
import javafx.scene.text.FontWeight
import org.example.Generator.ContainerManager
import org.example.MainMenu
import tornadofx.*

class ContainerManagementView : View("Container Management") {
    private val containerManager = ContainerManager()
    private val availableImages = FXCollections.observableArrayList<String>()
    private val selectedImage = SimpleStringProperty()
    private val containerName = SimpleStringProperty()
    private val ports = SimpleStringProperty()
    private val envVars = SimpleStringProperty()
    private val volumes = SimpleStringProperty()

    override val root = vbox {
        paddingAll = 20
        spacing = 10.0

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
                    val containers = containerManager.listContainers()
                    alert(Alert.AlertType.INFORMATION, "Running Containers", containers.joinToString("\n"))
                }
            }
            button("List All Containers") {
                action {
                    val containers = containerManager.listContainers(true)
                    alert(Alert.AlertType.INFORMATION, "All Containers", containers.joinToString("\n"))
                }
            }
        }

        label("Run New Container") {
            style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }
        }

        hbox {
            spacing = 10.0
            combobox(selectedImage, availableImages) {
                promptText = "Select Docker Image"
                prefWidth = 300.0
            }
            button("Refresh Images") {
                action {
                    availableImages.setAll(containerManager.listImages())
                }
            }
        }

        textfield(containerName) {
            promptText = "Container Name"
            prefWidth = 300.0
        }

        textfield(ports) {
            promptText = "Ports (format: hostPort:containerPort,...)"
            prefWidth = 300.0
        }

        textfield(envVars) {
            promptText = "Environment Variables (format: KEY=VALUE,...)"
            prefWidth = 300.0
        }

        textfield(volumes) {
            promptText = "Volumes (format: hostPath:containerPath,...)"
            prefWidth = 300.0
        }

        button("Run Container") {
            action {
                val imgName = selectedImage.get()
                val contName = containerName.get()
                val portMappings = ports.get().split(",").filter { it.isNotBlank() }
                val environmentVariables = envVars.get().split(",").filter { it.isNotBlank() }
                val volumeMappings = volumes.get().split(",").filter { it.isNotBlank() }

                if (imgName.isNullOrEmpty() || contName.isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide both image name and container name.")
                } else {
                    val success = containerManager.runContainer(imgName, contName, portMappings, environmentVariables, volumeMappings)
                    if (success) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Container started successfully.")
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed to start container.")
                    }
                }
            }
        }

        label("Stop and Remove Container") {
            style {
                fontSize = 16.px
                fontWeight = FontWeight.BOLD
            }
        }

        hbox {
            spacing = 10.0
            val stopContainerName = SimpleStringProperty()
            textfield(stopContainerName) {
                promptText = "Container Name"
                prefWidth = 300.0
            }
            button("Stop Container") {
                action {
                    val contName = stopContainerName.get()
                    if (contName.isNullOrEmpty()) {
                        alert(Alert.AlertType.WARNING, "Warning", "Please provide a container name.")
                    } else {
                        val success = containerManager.stopContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container stopped successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Failed to stop container.")
                        }
                    }
                }
            }
            button("Remove Container") {
                action {
                    val contName = stopContainerName.get()
                    if (contName.isNullOrEmpty()) {
                        alert(Alert.AlertType.WARNING, "Warning", "Please provide a container name.")
                    } else {
                        val success = containerManager.removeContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container removed successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Failed to remove container.")
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

    init {
        availableImages.setAll(containerManager.listImages())
    }
}
