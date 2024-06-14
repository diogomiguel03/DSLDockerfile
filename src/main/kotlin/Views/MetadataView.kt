// File: MetadataView.kt
package org.example.Views

import javafx.scene.control.Alert
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import org.example.Controllers.MetadataController
import org.example.MainMenu
import tornadofx.*
import java.io.File

class MetadataView : View("Metadata Viewer") {
    private val controller = MetadataController()

    override val root = vbox {
        spacing = 10.0
        paddingAll = 20.0

        button("Select Directory") {
            action {
                val directoryChooser = DirectoryChooser()
                directoryChooser.title = "Select Directory"
                val selectedDir = directoryChooser.showDialog(null)
                if (selectedDir != null) {
                    controller.selectedDirectory.set(selectedDir.absolutePath)
                    controller.findAndDisplayMetadataFiles(selectedDir)
                } else {
                    alert(Alert.AlertType.WARNING, "Warning", "No directory selected.")
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
