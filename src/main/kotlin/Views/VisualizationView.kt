package org.example.Views

import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.stage.DirectoryChooser
// import org.example.Generator.DockerProjectLoader
import org.example.model.*
import tornadofx.*

/*
class VisualizationView : View("Docker Project Visualization") {

    private var project: Project? = null
    private val treeView = TreeView<String>()
    private val detailsView = vbox()

    override val root = borderpane {
        top = hbox {
            alignment = Pos.CENTER
            spacing = 20.0
            button("Select Folder") {
                action {
                    val directoryChooser = DirectoryChooser()
                    val selectedDirectory = directoryChooser.showDialog(null)
                    if (selectedDirectory != null) {
                        val loader = DockerProjectLoader()
                        project = loader.loadProjectFromFolder(selectedDirectory.absolutePath)
                        populateTreeView()
                    }
                }
            }
        }
        left = treeView
        center = detailsView
    }

    private fun populateTreeView() {
        val rootItem = TreeItem("Project")
        project?.let { project ->
            val dockerfilesItem = TreeItem("Dockerfiles")
            project.dockerfiles.forEach { dockerfile ->
                val dockerfileItem = TreeItem(dockerfile.name)
                val imagesForDockerfile = project.dockerImages.filter { it.dockerfile == dockerfile }
                imagesForDockerfile.forEach { image ->
                    val imageItem = TreeItem("Image: ${image.name}")
                    val containersForImage = project.dockerContainers.filter { it.image == image }
                    containersForImage.forEach { container ->
                        imageItem.children.add(TreeItem("Container: ${container.name}"))
                    }
                    dockerfileItem.children.add(imageItem)
                }
                dockerfilesItem.children.add(dockerfileItem)
            }

            rootItem.children.add(dockerfilesItem)
        }
        treeView.root = rootItem
        treeView.isShowRoot = false

        treeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { displayDetails(it.value) }
        }
    }

    private fun displayDetails(selectedItem: String) {
        detailsView.clear()
        project?.let { project ->
            val dockerfile = project.dockerfiles.find { it.name == selectedItem }
            val dockerImage = project.dockerImages.find { it.name == selectedItem }
            val dockerContainer = project.dockerContainers.find { it.name == selectedItem }

            when {
                dockerfile != null -> {
                    detailsView.add(label("Dockerfile: ${dockerfile.name}"))
                    project.dockerImages.filter { it.dockerfile == dockerfile }.forEach { image ->
                        detailsView.add(label("  -> Docker Image: ${image.name}"))
                        project.dockerContainers.filter { it.image == image }.forEach { container ->
                            detailsView.add(label("      -> Docker Container: ${container.name}"))
                        }
                    }
                }
                dockerImage != null -> {
                    detailsView.add(label("Docker Image: ${dockerImage.name}"))
                    dockerImage.dockerfile?.let {
                        detailsView.add(label("  From Dockerfile: ${it.name}"))
                    }
                    project.dockerContainers.filter { it.image == dockerImage }.forEach { container ->
                        detailsView.add(label("  -> Docker Container: ${container.name}"))
                    }
                }
                dockerContainer != null -> {
                    detailsView.add(label("Docker Container: ${dockerContainer.name}"))
                    detailsView.add(label("  From Docker Image: ${dockerContainer.image?.name}"))
                }
            }
        }
    }
}

 */