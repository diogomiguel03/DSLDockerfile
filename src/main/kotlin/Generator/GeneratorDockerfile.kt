package org.example.Generator

import javafx.beans.property.SimpleStringProperty
import org.example.DSL.InstructionsDockerfile
import java.io.File
import java.util.*

class GeneratorDockerfile(val instructions: InstructionsDockerfile) {
    private val scanner = Scanner(System.`in`)
    val previewContent = SimpleStringProperty()

    fun createDefaultDockerfile(image: String, tag: String, updateCommand: String, installCommand: String, workdirPath: String, copySource: String, copyDestination: String, compileCommand: String, cmdCommand: String, filePath: String) {
        instructions.from(image, tag)
        instructions.run(updateCommand)
        instructions.run(installCommand)
        instructions.workdir(workdirPath)
        instructions.copy(copySource, copyDestination)
        instructions.run(compileCommand)
        instructions.cmd(cmdCommand)
        generate(filePath)
        println("Dockerfile created at $filePath")
    }

    fun createCustomDockerfile(instruction: String, parameters: String? = null, filePath: String? = null) {
        if (parameters != null) {
            // Add instruction
            when (instruction) {
                "FROM" -> {
                    val parts = parameters.split(" ")
                    val image = parts[0]
                    val tag = if (parts.size > 1) parts[1] else null
                    instructions.from(image, tag)
                }
                "ADD" -> {
                    val (source, destination) = parameters.split(" ")
                    instructions.add(source, destination)
                }
                "ARG" -> {
                    val (name, defaultValue) = parameters.split(" ")
                    instructions.arg(name, defaultValue)
                }
                "CMD" -> instructions.cmd(parameters)
                "ENTRYPOINT" -> instructions.entrypoint(parameters)
                "ENV" -> {
                    val (key, value) = parameters.split(" ")
                    instructions.env(key, value)
                }
                "EXPOSE" -> instructions.expose(parameters.toInt())
                "HEALTHCHECK" -> instructions.healthcheck(parameters)
                "LABEL" -> {
                    val (key, value) = parameters.split(" ")
                    instructions.label(key, value)
                }
                "MAINTAINER" -> instructions.maintainer(parameters)
                "RUN" -> instructions.run(parameters)
                "SHELL" -> instructions.shell(parameters)
                "STOPSIGNAL" -> instructions.stopsignal(parameters)
                "USER" -> instructions.user(parameters)
                "VOLUME" -> instructions.volume(parameters)
                "WORKDIR" -> instructions.workdir(parameters)
                "COPY" -> {
                    val (source, destination) = parameters.split(" ")
                    instructions.copy(source, destination)
                }
                else -> throw IllegalArgumentException("Unsupported instruction: $instruction")
            }
            updatePreview()
        } else if (filePath != null) {
            // Generate Dockerfile
            val content = instructions.build()
            File(filePath).writeText(content)
        } else {
            throw IllegalArgumentException("Either parameters or filePath must be provided")
        }
    }

    private fun readInput(prompt: String, defaultValue: String = ""): String {
        print("$prompt ")
        val input = scanner.nextLine()
        return if (input.isBlank()) defaultValue else input
    }

    private fun generate(filePath: String) {
        val content = instructions.build()
        File(filePath).writeText(content)
    }

    fun editDockerfile() {
        println("Enter file path of Dockerfile to edit:")
        val filePath = scanner.nextLine()
        val file = File(filePath)
        if (!file.exists()) {
            println("File does not exist.")
            return
        }

        val originalInstructions = file.readLines().toMutableList()
        println("Original Dockerfile:")
        originalInstructions.forEachIndexed { index, line -> println("${index + 1}. $line") }

        println("Enter line number to edit (starting from 1):")
        val lineNumber = scanner.nextLine().toInt() - 1
        if (lineNumber !in originalInstructions.indices) {
            println("Invalid line number.")
            return
        }

        val instruction = originalInstructions[lineNumber]
        when {
            instruction.startsWith("FROM") -> {
                val image = readInput("Enter new image for FROM instruction:")
                val tag = readInput("Enter new tag for FROM instruction (leave blank for 'latest'):", "latest")
                originalInstructions[lineNumber] = instructions.from(image, tag)
            }
            instruction.startsWith("RUN") -> {
                val command = readInput("Enter new command for RUN instruction:")
                originalInstructions[lineNumber] = "RUN $command"
            }
            instruction.startsWith("COPY") -> {
                val source = readInput("Enter new source for COPY instruction:")
                val destination = readInput("Enter new destination for COPY instruction:")
                originalInstructions[lineNumber] = "COPY $source $destination"
            }
            // Add cases for other instructions as needed
            else -> {
                println("Unsupported instruction.")
                return
            }
        }

        file.writeText(originalInstructions.joinToString("\n"))
        println("Dockerfile updated.")
    }

    // Method to update the preview content
    private fun updatePreview() {
        val dockerfileContent = instructions.build()
        previewContent.set(dockerfileContent)
    }
}
