// File: main.kt
package org.example

import tornadofx.App
import tornadofx.launch
import javafx.stage.Stage

class MyApp : App(MainMenu::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }
}

fun main(args: Array<String>) {
    launch<MyApp>(args)
}

