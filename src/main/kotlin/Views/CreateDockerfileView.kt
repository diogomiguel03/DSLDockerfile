// File: CreateDockerfileView.kt
package org.example

import org.example.Views.CreateCustomDockerfileView
import tornadofx.*
import org.example.Views.CreateDefaultDockerfileView

class CreateDockerfileView : View("Create Dockerfile") {

    override val root = vbox {
        button("Create Default Dockerfile (recommended)") {
            action {
                replaceWith<CreateDefaultDockerfileView>()
            }
        }

        button("Create custom Dockerfile") {
            action {
                replaceWith<CreateCustomDockerfileView>()
            }
        }

        button("Back") {
            action {
                replaceWith<MainMenu>()
            }
        }
    }
}
