// File: EditDockerfileView.kt
package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.text.FontWeight
import javafx.stage.FileChooser
import org.example.Controllers.DockerfileController
import org.example.MainMenu
import tornadofx.*
import java.io.File

class EditDockerfileView : View("Edit Dockerfile") {
    private val selectedFilePath = SimpleStringProperty()
    private val dockerfileContent = SimpleStringProperty()
    private val controller = DockerfileController()

    override val root = vbox {
        spacing = 10.0
        paddingAll = 20.0

        hbox {
            spacing = 10.0
            label("Select Dockerfile:")
            textfield(selectedFilePath) {
                isEditable = false
                prefWidth = 300.0
            }
            button("Browse") {
                action {
                    val fileChooser = FileChooser()
                    fileChooser.title = "Select Dockerfile"
                    val selectedFile = fileChooser.showOpenDialog(null)
                    if (selectedFile != null) {
                        selectedFilePath.set(selectedFile.absolutePath)
                        controller.loadDockerfile(selectedFile, dockerfileContent)
                    }
                }
            }
        }

        label("Dockerfile Content:") {
            style {
                fontWeight = FontWeight.BOLD
            }
        }
        textarea(dockerfileContent) {
            isEditable = true
            prefRowCount = 20
            prefColumnCount = 60
        }

        hbox {
            spacing = 10.0
            button("Save Changes") {
                action {
                    controller.saveChanges(selectedFilePath, dockerfileContent)
                }
            }
            button("Back") {
                action {
                    replaceWith<MainMenu>()
                }
            }
        }
    }
}
