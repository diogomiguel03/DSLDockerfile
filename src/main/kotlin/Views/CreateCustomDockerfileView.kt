package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.stage.FileChooser
import org.example.CreateDockerfileView
import org.example.DSL.InstructionsDockerfile
import org.example.Generator.GeneratorDockerfile
import tornadofx.*

class CreateCustomDockerfileView : View("Create Custom Dockerfile") {
    private val instructionTypes = listOf(
        "FROM", "ADD", "ARG", "CMD", "ENTRYPOINT", "ENV", "EXPOSE",
        "HEALTHCHECK", "LABEL", "MAINTAINER", "RUN", "SHELL", "STOPSIGNAL",
        "USER", "VOLUME", "WORKDIR", "COPY"
    )

    private val selectedInstruction = SimpleStringProperty()
    private val parameterField = SimpleStringProperty()
    private var instructions = InstructionsDockerfile()
    private var generator = GeneratorDockerfile(instructions)

    override val root = vbox {
        label("Select Instruction:")
        combobox(selectedInstruction, instructionTypes)

        label("Enter Parameters:")
        textfield(parameterField)

        button("Add Instruction") {
            action {
                addInstruction()
            }
        }

        button("Generate Dockerfile") {
            action {
                generateDockerfile()
            }
        }

        button("Back") {
            action {
                replaceWith<CreateDockerfileView>()
            }
        }
    }

    private fun addInstruction() {
        val instruction = selectedInstruction.value
        val parameters = parameterField.value
        if (instruction != null && parameters.isNotEmpty()) {
            try {
                generator.createCustomDockerfile(instruction, parameters)
                parameterField.set("")
                alert(Alert.AlertType.INFORMATION, "Success", "Instruction added: $instruction $parameters")
            } catch (e: Exception) {
                alert(Alert.AlertType.ERROR, "Error", e.message ?: "Unknown error")
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", "Please select an instruction and enter parameters")
        }
    }

    private fun generateDockerfile() {
        val fileChooser = FileChooser()
        fileChooser.title = "Save Dockerfile"
        fileChooser.initialFileName = "Dockerfile"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Dockerfile", "*.*")
        )
        val selectedFile = fileChooser.showSaveDialog(null)
        if (selectedFile != null) {
            try {
                val filePath = selectedFile.absolutePath
                generator.createCustomDockerfile(instruction = "", filePath = filePath)
                alert(Alert.AlertType.INFORMATION, "Success", "Dockerfile created at $filePath")

                // Reset instructions and generator for the next creation
                instructions = InstructionsDockerfile()
                generator = GeneratorDockerfile(instructions)

                replaceWith<CreateDockerfileView>()
            } catch (e: Exception) {
                alert(Alert.AlertType.ERROR, "Error", "Failed to generate Dockerfile: ${e.message}")
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", "File not selected")
        }
    }
}
