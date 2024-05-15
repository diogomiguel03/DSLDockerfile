package org.example

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(javaClass.getResource("/main.fxml"))
        val root = loader.load<Parent>()
        primaryStage.title = "Dockerfile Generator"
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}
