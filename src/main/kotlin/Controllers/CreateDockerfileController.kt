package org.example.Controllers

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import org.example.DSL.InstructionsDockerfile

class CreateDockerfileController {

    @FXML
    private lateinit var imageTextField: TextField

    @FXML
    private lateinit var tagTextField: TextField

    @FXML
    private lateinit var instructionsListView: ListView<String>

    private val instructionsDockerfile = InstructionsDockerfile()
    private val instructionsList = FXCollections.observableArrayList<String>()

    @FXML
    fun initialize() {
        instructionsListView.items = instructionsList
    }

    @FXML
    private fun onAddFrom() {
        val image = imageTextField.text
        val tag = if (tagTextField.text.isEmpty()) "latest" else tagTextField.text
        val instruction = instructionsDockerfile.from(image, tag)
        instructionsList.add(instruction)
        clearFields()
    }

    @FXML
    private fun onSave() {
        val dockerfileContent = instructionsDockerfile.build()
        // Save dockerfileContent to a file
        println(dockerfileContent as Any) // Fixing overload ambiguity
        // Add file saving logic here
    }

    @FXML
    private fun onClear() {
        instructionsDockerfile.instructions.clear()
        instructionsList.clear()
    }

    private fun clearFields() {
        imageTextField.clear()
        tagTextField.clear()
    }
}
