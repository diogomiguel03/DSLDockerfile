package org.example

import org.example.DSL.InstructionsDockerfile
import org.example.Generator.GeneratorDockerImage

import java.util.*

class Menu {
    private val scanner = Scanner(System.`in`)

    fun start() {
        while (true) {
            println("1. Create Dockerfile")
            println("2. Edit a Dockerfile")
            println("3. Create Docker image")
            println("4. Exit")
            when (scanner.nextLine()) {
                "1" -> {
                    val instructions = InstructionsDockerfile()
                    val generator = GeneratorDockerfile(instructions)
                    generator.createDockerfile()
                }
                "2" -> {
                    val instructions = InstructionsDockerfile()
                    val generator = GeneratorDockerfile(instructions)
                    generator.editDockerfile()
                }
                "3" -> {
                    val generator = GeneratorDockerImage()
                    generator.buildImage()
                }
                "4" -> return
                else -> println("Invalid option")
            }
        }
    }
}
