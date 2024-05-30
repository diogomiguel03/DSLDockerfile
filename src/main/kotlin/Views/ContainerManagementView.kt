package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import org.example.Generator.ContainerManager
import org.example.MainMenu
import tornadofx.*

class ContainerManagementView : View("Container Management") {
    private val containerManager = ContainerManager()
    private val availableImages = FXCollections.observableArrayList<String>()
    private val selectedImage = SimpleStringProperty()
    private val availableContainers = FXCollections.observableArrayList<String>()
    private val selectedContainer = SimpleStringProperty()
    private val containerName = SimpleStringProperty()
    private val ports = SimpleStringProperty()
    private val envVars = SimpleStringProperty()
    private val volumes = SimpleStringProperty()

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
                    val containers = containerManager.listContainers()
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
                    val containers = containerManager.listContainers(true)
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

        textfield(containerName) {
            promptText = "Container Name"
            prefWidth = 300.0
        }

        hbox {
            spacing = 10.0
            combobox(selectedImage, availableImages) {
                promptText = "Select Docker Image (optional)"
                prefWidth = 300.0
            }
            button("Refresh Images") {
                action {
                    availableImages.setAll(containerManager.listImages())
                }
                style {
                    backgroundColor += c("#bde0fe")
                    paddingAll = 10
                    borderRadius += box(10.px)
                }
            }
        }

        textfield(ports) {
            promptText = "Ports (optional, format: hostPort:containerPort,...)"
            prefWidth = 300.0
        }

        textfield(envVars) {
            promptText = "Environment Variables (optional, format: KEY=VALUE,...)"
            prefWidth = 300.0
        }

        textfield(volumes) {
            promptText = "Volumes (optional, format: hostPath:containerPath,...)"
            prefWidth = 300.0
        }

        button("Run Container") {
            action {
                val contName = containerName.get()
                val imgName = selectedImage.get()
                val portMappings = ports.get()?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
                val environmentVariables = envVars.get()?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
                val volumeMappings = volumes.get()?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

                if (contName.isNullOrEmpty()) {
                    alert(Alert.AlertType.WARNING, "Warning", "Please provide a container name.")
                } else {
                    val success = containerManager.runContainer(contName, imgName, portMappings, environmentVariables, volumeMappings)
                    if (success) {
                        alert(Alert.AlertType.INFORMATION, "Success", "Container started successfully.")
                    } else {
                        alert(Alert.AlertType.ERROR, "Error", "Failed to start container.")
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
                    availableContainers.setAll(containerManager.listContainers(true))
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
                        val success = containerManager.stopContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container stopped successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Failed to stop container.")
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
                        val success = containerManager.removeContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container removed successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Failed to remove container.")
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
                        val success = containerManager.startContainer(contName)
                        if (success) {
                            alert(Alert.AlertType.INFORMATION, "Success", "Container started successfully.")
                        } else {
                            alert(Alert.AlertType.ERROR, "Error", "Failed to start container.")
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
        availableImages.setAll(containerManager.listImages())
        availableContainers.setAll(containerManager.listContainers(true)) // Load all containers initially
    }
}