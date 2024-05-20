package org.example

import javafx.application.Platform
import org.example.Views.ContainerManagementView
import org.example.Views.CreateDockerImageView
import tornadofx.*

class MainMenu : View("Main Menu") {
    override val root = vbox {
        paddingAll = 20
        spacing = 10.0

        button("Create Dockerfile") {
            action {
                replaceWith<CreateDockerfileView>()
            }
        }

        button("Create Docker Image") {
            action {
                replaceWith<CreateDockerImageView>()
            }
        }

        button("Manage Containers") {
            action {
                replaceWith<ContainerManagementView>()
            }
        }

        button("Exit") {
            action {
                Platform.exit()
            }
        }
    }
}
