// File: CreateDefaultDockerfileView.kt
package org.example.Views

import javafx.stage.FileChooser
import tornadofx.*
import org.example.Controllers.DockerfileController
import org.example.MainMenu

class CreateDefaultDockerfileView : View("Create Default Dockerfile") {
    private val controller = DockerfileController()

    override val root = form {
        fieldset("Create Default Dockerfile") {
            val imageField = textfield()
            field("FROM instruction - Image:") {
                this += imageField
            }
            val tagField = textfield()
            field("FROM instruction - Tag:") {
                this += tagField
            }
            val updateCommandField = textfield()
            field("RUN instruction (update) - Command:") {
                this += updateCommandField
            }
            val installCommandField = textfield()
            field("RUN instruction (install) - Command:") {
                this += installCommandField
            }
            val workdirPathField = textfield()
            field("WORKDIR instruction - Path:") {
                this += workdirPathField
            }
            val copySourceField = textfield()
            field("COPY instruction - Source:") {
                this += copySourceField
            }
            val copyDestinationField = textfield()
            field("COPY instruction - Destination:") {
                this += copyDestinationField
            }
            val compileCommandField = textfield()
            field("RUN instruction (compile) - Command:") {
                this += compileCommandField
            }
            val cmdCommandField = textfield()
            field("CMD instruction - Command:") {
                this += cmdCommandField
            }

            button("Generate Dockerfile") {
                action {
                    val fileChooser = FileChooser()
                    fileChooser.title = "Save Dockerfile"
                    fileChooser.initialFileName = "Dockerfile"
                    fileChooser.extensionFilters.addAll(
                        FileChooser.ExtensionFilter("Dockerfile", "*.*")
                    )
                    val selectedFile = fileChooser.showSaveDialog(null)
                    if (selectedFile != null) {
                        if (imageField.text.isBlank() ||
                            updateCommandField.text.isBlank() ||
                            installCommandField.text.isBlank() ||
                            workdirPathField.text.isBlank() ||
                            copySourceField.text.isBlank() ||
                            copyDestinationField.text.isBlank() ||
                            compileCommandField.text.isBlank() ||
                            cmdCommandField.text.isBlank()) {
                            error("Error", "All fields must be filled")
                        } else {
                            try {
                                controller.createDefaultDockerfile(
                                    imageField.text,
                                    tagField.text,
                                    updateCommandField.text,
                                    installCommandField.text,
                                    workdirPathField.text,
                                    copySourceField.text,
                                    copyDestinationField.text,
                                    compileCommandField.text,
                                    cmdCommandField.text,
                                    selectedFile.absolutePath
                                )
                                information("Success", "Dockerfile created at ${selectedFile.absolutePath}")
                                replaceWith<MainMenu>()
                            } catch (e: Exception) {
                                error("Error", "Failed to create Dockerfile: ${e.message}")
                            }
                        }
                    } else {
                        println("File not selected")
                    }
                }
            }
        }
    }
}
