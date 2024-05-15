package org.example

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.control.Alert

class MainController {

    @FXML
    private fun onCreateDockerfile(event: ActionEvent) {
        navigateTo("/createDockerfile.fxml", "Create Dockerfile")
    }

    @FXML
    private fun onEditDockerfile(event: ActionEvent) {
        showAlert("Edit Dockerfile", "This feature is not yet implemented.")
    }

    @FXML
    private fun onBuildDockerImage(event: ActionEvent) {
        showAlert("Build Docker Image", "This feature is not yet implemented.")
    }

    @FXML
    private fun onExit(event: ActionEvent) {
        System.exit(0)
    }

    private fun navigateTo(fxml: String, title: String) {
        try {
            val loader = FXMLLoader(javaClass.getResource(fxml))
            val root = loader.load<Parent>()
            val stage = Stage()
            stage.title = title
            stage.scene = Scene(root)
            stage.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlert(title: String, message: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}
