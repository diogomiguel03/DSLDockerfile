package org.example.Views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.stage.FileChooser
import org.example.CreateDockerfileView
import org.example.DSL.InstructionsDockerfile
import org.example.Generator.GeneratorDockerfile
import tornadofx.*
import java.io.File

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
                when (instruction) {
                    "FROM" -> {
                        val parts = parameters.split(" ", limit = 2)
                        if (parts.size == 2) {
                            instructions.from(parts[0], parts[1])
                        } else {
                            throw IllegalArgumentException("FROM requires 'image tag'")
                        }
                    }
                    "ADD", "COPY" -> {
                        val parts = parameters.split(" ", limit = 2)
                        if (parts.size == 2) {
                            if (instruction == "ADD") {
                                instructions.add(parts[0], parts[1])
                            } else {
                                instructions.copy(parts[0], parts[1])
                            }
                        } else {
                            throw IllegalArgumentException("$instruction requires 'source destination'")
                        }
                    }
                    "ARG" -> {
                        val parts = parameters.split(" ", limit = 2)
                        if (parts.size == 2) {
                            instructions.arg(parts[0], parts[1])
                        } else {
                            throw IllegalArgumentException("ARG requires 'name defaultValue'")
                        }
                    }
                    "ENV", "LABEL" -> {
                        val parts = parameters.split(" ", limit = 2)
                        if (parts.size == 2) {
                            if (instruction == "ENV") {
                                instructions.env(parts[0], parts[1])
                            } else {
                                instructions.label(parts[0], parts[1])
                            }
                        } else {
                            throw IllegalArgumentException("$instruction requires 'key value'")
                        }
                    }
                    "EXPOSE" -> instructions.expose(parameters.toInt())
                    "HEALTHCHECK" -> instructions.healthcheck(parameters)
                    "CMD", "ENTRYPOINT", "RUN", "SHELL", "STOPSIGNAL", "USER", "WORKDIR" -> {
                        when (instruction) {
                            "CMD" -> instructions.cmd(parameters)
                            "ENTRYPOINT" -> instructions.entrypoint(parameters)
                            "RUN" -> instructions.run(parameters)
                            "SHELL" -> instructions.shell(parameters)
                            "STOPSIGNAL" -> instructions.stopsignal(parameters)
                            "USER" -> instructions.user(parameters)
                            "WORKDIR" -> instructions.workdir(parameters)
                        }
                    }
                    "MAINTAINER" -> instructions.maintainer(parameters)
                    "VOLUME" -> instructions.volume(parameters)
                    else -> throw IllegalArgumentException("Unsupported instruction: $instruction")
                }
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
                val content = instructions.build()
                File(filePath).writeText(content)
                alert(Alert.AlertType.INFORMATION, "Success", "Dockerfile created at $filePath")
                instructions = InstructionsDockerfile() // Reset instructions for the next creation
                generator = GeneratorDockerfile(instructions) // Reset generator with the new instructions
                replaceWith<CreateDockerfileView>()
            } catch (e: Exception) {
                alert(Alert.AlertType.ERROR, "Error", "Failed to generate Dockerfile: ${e.message}")
            }
        } else {
            alert(Alert.AlertType.WARNING, "Warning", "File not selected")
        }
    }
}
