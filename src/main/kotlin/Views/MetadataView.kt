package org.example.Views

import javafx.scene.control.Alert
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import org.example.Controllers.MetadataController
import org.example.MainMenu
import tornadofx.*

class MetadataView : View("Metadata Viewer") {
    private val controller = MetadataController()

    override val root = vbox {
        spacing = 10.0
        paddingAll = 20.0

        button("Select Directory") {
            action {
                try {
                    val directoryChooser = DirectoryChooser()
                    directoryChooser.title = "Select Directory"
                    val selectedDir = directoryChooser.showDialog(currentWindow)
                    if (selectedDir != null) {
                        controller.selectedDirectory.set(selectedDir.absolutePath)
                        controller.findAndDisplayMetadataFiles(selectedDir)
                    } else {
                        alert(Alert.AlertType.WARNING, "Warning", "No directory selected.")
                    }
                } catch (e: Exception) {
                    alert(Alert.AlertType.ERROR, "Error", "An error occurred while selecting the directory: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        label("Selected Directory:")
        textfield(controller.selectedDirectory) {
            isEditable = false
            prefWidth = 400.0
        }

        label("Metadata Content:")
        scrollpane {
            content = controller.metadataContent
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }
}
