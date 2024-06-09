// File: DockerComposeView.kt
package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import org.example.Controllers.DockerComposeController
import org.example.DSL.DockerComposeService
import org.example.MainMenu
import tornadofx.*

class DockerComposeView : View("Create Docker Compose File") {
    private val controller = DockerComposeController()

    private val serviceName = SimpleStringProperty()
    private val imageName = SimpleStringProperty()
    private val ports = SimpleStringProperty()
    private val envVars = SimpleStringProperty()
    private val volumes = SimpleStringProperty()

    override val root = vbox {
        spacing = 10.0
        paddingAll = 20.0

        label("Service Name:")
        textfield(serviceName)

        label("Image Name:")
        textfield(imageName)

        label("Ports (format: hostPort:containerPort,...):")
        textfield(ports)

        label("Environment Variables (format: KEY=VALUE,...):")
        textfield(envVars)

        label("Volumes (format: hostPath:containerPath,...):")
        textfield(volumes)

        hbox {
            spacing = 10.0
            button("Add Service") {
                action {
                    addService()
                }
            }
            button("Generate Docker Compose File") {
                action {
                    generateComposeFile()
                }
            }
        }

        label("Docker Compose File Preview:") {
            style {
                fontWeight = FontWeight.BOLD
            }
        }
        textarea(controller.previewContent) {
            isEditable = false
            prefRowCount = 10
            prefColumnCount = 40
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }

    private fun addService() {
        val service = DockerComposeService(serviceName.get())
        service.image = imageName.get()
        service.ports.addAll(ports.get().split(",").filter { it.isNotBlank() })
        service.environment.putAll(envVars.get().split(",").filter { it.isNotBlank() }.associate {
            val (key, value) = it.split("=")
            key to value
        })
        service.volumes.addAll(volumes.get().split(",").filter { it.isNotBlank() })

        controller.addService(service)
        serviceName.set("")
        imageName.set("")
        ports.set("")
        envVars.set("")
        volumes.set("")
        alert(Alert.AlertType.INFORMATION, "Success", "Service added: ${service.name}")
    }

    private fun generateComposeFile() {
        val fileChooser = FileChooser()
        fileChooser.title = "Save Docker Compose File"
        fileChooser.initialFileName = "docker-compose.yml"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("YAML Files", "*.yml", "*.yaml")
        )
        val selectedFile = fileChooser.showSaveDialog(null)
        if (selectedFile != null) {
            try {
                controller.generate(selectedFile.absolutePath)
                alert(Alert.AlertType.INFORMATION, "Success", "Docker Compose file created at ${selectedFile.absolutePath}")

                // Reset controller for the next creation
                controller.resetComposeFile()

                replaceWith<MainMenu>()
            } catch (e: Exception) {
                alert(Alert.AlertType.ERROR, "Error", "Failed to generate Docker Compose file: ${e.message}")
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", "File not selected")
        }
    }
}
