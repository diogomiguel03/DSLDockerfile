// File: MainMenu.kt
package org.example

import javafx.application.Platform
import javafx.scene.paint.Color
import javafx.scene.layout.Priority
import tornadofx.*

class MainMenu : View("Main Menu") {
    override val root = vbox {
        minWidth = 400.0
        minHeight = 400.0
        alignment = javafx.geometry.Pos.CENTER

        button("Create Dockerfile") {
            action {
                replaceWith<CreateDockerfileView>()
            }
            style {
                fontSize = 16.px
                padding = box(10.px, 20.px)
                backgroundColor += c("#4CAF50")
                borderRadius += box(5.px)
            }
            vboxConstraints {
                marginBottom = 10.0
            }
        }
        button("Edit a Dockerfile") {
            action {
                // Handle "Edit a Dockerfile" action
            }
            style {
                fontSize = 16.px
                padding = box(10.px, 20.px)
                backgroundColor += c("#FFC107")
                borderRadius += box(5.px)
            }
            vboxConstraints {
                marginBottom = 10.0
            }
        }
        button("Create Docker image") {
            action {
                // Handle "Create Docker image" action
            }
            style {
                fontSize = 16.px
                padding = box(10.px, 20.px)
                backgroundColor += c("#2196F3")
                borderRadius += box(5.px)
            }
            vboxConstraints {
                marginBottom = 10.0
            }
        }
        button("Exit") {
            action {
                close()
            }
            style {
                fontSize = 16.px
                padding = box(10.px, 20.px)
                backgroundColor += c("#F44336")
                borderRadius += box(5.px)
            }
        }
    }
}
