package org.example

import javafx.application.Platform
import javafx.geometry.Pos
import org.example.Views.ContainerManagementView
import org.example.Views.CreateDockerImageView
import tornadofx.*

class MainMenu : View("Main Menu") {
    override val root = borderpane {
        style {
            backgroundColor += c("#e0e1dd")
            paddingAll = 20
        }

        center = vbox {
            alignment = Pos.CENTER
            spacing = 20.0

            hbox {
                spacing = 20.0
                alignment = Pos.CENTER

                button("Create Dockerfile") {
                    action {
                        replaceWith<CreateDockerfileView>()
                    }
                    style {
                        backgroundColor += c("#bde0fe")
                        textFill = c("#000000") // black text
                        fontSize = 16.px
                        padding = box(15.px, 30.px)
                        borderRadius += box(15.px)
                        borderColor += box(c("#bde0fe"))
                        borderWidth += box(2.px)
                        minWidth = 150.px
                        alignment = Pos.CENTER
                    }
                }

                button("Create Docker Image") {
                    action {
                        replaceWith<CreateDockerImageView>()
                    }
                    style {
                        backgroundColor += c("#bde0fe")
                        textFill = c("#000000") // black text
                        fontSize = 16.px
                        padding = box(15.px, 30.px)
                        borderRadius += box(15.px)
                        borderColor += box(c("#bde0fe"))
                        borderWidth += box(2.px)
                        minWidth = 150.px
                        alignment = Pos.CENTER
                    }
                }

                button("Manage Containers") {
                    action {
                        replaceWith<ContainerManagementView>()
                    }
                    style {
                        backgroundColor += c("#bde0fe")
                        textFill = c("#000000") // black text
                        fontSize = 16.px
                        padding = box(15.px, 30.px)
                        borderRadius += box(15.px)
                        borderColor += box(c("#bde0fe"))
                        borderWidth += box(2.px)
                        minWidth = 150.px
                        alignment = Pos.CENTER
                    }
                }
            }
        }

        bottom = vbox {
            alignment = Pos.CENTER
            spacing = 20.0

            label("Exit") {
                style {
                    textFill = c("#ff0000") // red text
                    fontSize = 16.px
                    cursor = javafx.scene.Cursor.HAND
                }
                setOnMouseClicked {
                    Platform.exit()
                }
            }
        }
    }
}
