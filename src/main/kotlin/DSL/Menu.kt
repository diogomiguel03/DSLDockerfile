package org.example.DSL

import org.example.DSL.InstructionsDockerfile
import org.example.Generator.GeneratorDockerImage
import org.example.generator.GeneratorDockerfile

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
                "1" -> createDockerfile()
                "2" -> editDockerfile()
                "3" -> buildDockerImage()
                "4" -> return
                else -> println("Invalid option")
            }
        }
    }

    private fun createDockerfile() {
        println("1. Create default Dockerfile")
        println("2. Create custom Dockerfile")
        when (scanner.nextLine()) {
            "1" -> createDefaultDockerfile()
            "2" -> createCustomDockerfile()
            else -> println("Invalid option")
        }
    }

    private fun createDefaultDockerfile() {
        val instructions = InstructionsDockerfile()
        val generator = GeneratorDockerfile(instructions)
        generator.createDefaultDockerfile()
    }

    private fun createCustomDockerfile() {
        val instructions = InstructionsDockerfile()
        val generator = GeneratorDockerfile(instructions)
        generator.createCustomDockerfile()
    }

    private fun editDockerfile() {
        val instructions = InstructionsDockerfile()
        val generator = GeneratorDockerfile(instructions)
        generator.editDockerfile()
    }

    private fun buildDockerImage() {
        val generator = GeneratorDockerImage()
        generator.buildImage()
    }
}
