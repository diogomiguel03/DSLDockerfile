// File: CreateDockerfileView.kt
package org.example.Views

import org.example.MainMenu
import tornadofx.*

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
