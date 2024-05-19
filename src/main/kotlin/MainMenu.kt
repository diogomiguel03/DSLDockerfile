// File: MainMenu.kt
package org.example

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import org.example.Views.CreateDockerImageView
import tornadofx.*

class MainMenu : View("Main Menu") {
    override val root = stackpane {
        minWidth = 500.0
        minHeight = 500.0
        vbox {
            alignment = Pos.CENTER
            spacing = 20.0  // Add some space between elements in the vbox

            // Title
            label("Main Menu") {
                style {
                    fontFamily = "Poppins"
                    fontSize = 24.px
                    fontWeight = FontWeight.BOLD
                    textFill = Color.BLACK
                }
            }

            // Container for buttons
            stackpane {
                alignment = Pos.CENTER
                padding = Insets(20.0)
                vboxConstraints {
                    margin = Insets(20.0)
                }

                // Background rounded rectangle
                rectangle {
                    width = 400.0
                    height = 400.0
                    fill = Color.LIGHTBLUE
                    arcWidth = 20.0
                    arcHeight = 20.0
                }

                // Main content: Buttons
                vbox {
                    alignment = Pos.CENTER
                    spacing = 20.0
                    padding = Insets(20.0)

                    button("Create Dockerfile") {
                        action {
                            replaceWith<CreateDockerfileView>()
                        }
                        style {
                            fontFamily = "Poppins"
                            fontSize = 16.px
                            padding = box(10.px, 20.px)
                            backgroundColor += c("#4CAF50")
                            borderRadius += box(5.px)
                        }
                    }
                    button("Edit a Dockerfile") {
                        action {
                            // Handle "Edit a Dockerfile" action
                        }
                        style {
                            fontFamily = "Poppins"
                            fontSize = 16.px
                            padding = box(10.px, 20.px)
                            backgroundColor += c("#FFC107")
                            borderRadius += box(5.px)
                        }
                    }
                    button("Create Docker image") {
                        action {
                            replaceWith<CreateDockerImageView>()
                        }
                        style {
                            fontFamily = "Poppins"
                            fontSize = 16.px
                            padding = box(10.px, 20.px)
                            backgroundColor += c("#2196F3")
                            borderRadius += box(5.px)
                        }
                    }
                    button("Exit") {
                        action {
                            Platform.exit()
                        }
                        style {
                            fontFamily = "Poppins"
                            fontSize = 16.px
                            padding = box(10.px, 20.px)
                            backgroundColor += c("#F44336")
                            borderRadius += box(5.px)
                        }
                    }
                }
            }
        }
    }
}
