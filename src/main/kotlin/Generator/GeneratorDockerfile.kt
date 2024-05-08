package org.example

import org.example.DSL.InstructionsDockerfile
import java.io.File
import java.util.Scanner

class GeneratorDockerfile(private val instructions: InstructionsDockerfile) {
    private val scanner = Scanner(System.`in`)

    fun createDockerfile() {
        println("Enter image for FROM instruction:")
        val image = scanner.nextLine()
        println("Enter tag for FROM instruction (leave blank for 'latest'):")
        val tag = scanner.nextLine().ifBlank { "latest" }
        instructions.FROM(image, tag)

        println("Enter command for ENTRYPOINT instruction:")
        val command = scanner.nextLine()
        instructions.ENTRYPOINT(command)

        println("Enter port for EXPOSE instruction:")
        val port = scanner.nextLine().toInt()
        instructions.EXPOSE(port)

        println("Enter command for RUN instruction:")
        val runCommand = scanner.nextLine()
        instructions.RUN(runCommand)

        println("Enter path for WORKDIR instruction:")
        val path = scanner.nextLine()
        instructions.WORKDIR(path)

        println("Enter source and destination for COPY instruction (separated by a space):")
        val (source, destination) = scanner.nextLine().split(" ")
        instructions.COPY(source, destination)

        println("Enter file path for Dockerfile (leave blank for './Dockerfile'):")
        val filePath = scanner.nextLine().ifBlank { "./Dockerfile" }
        generate(filePath)

        println("Dockerfile created at $filePath")
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
                println("Enter new image for FROM instruction:")
                val image = scanner.nextLine()
                println("Enter new tag for FROM instruction (leave blank for 'latest'):")
                val tag = scanner.nextLine().ifBlank { "latest" }
                originalInstructions[lineNumber] = instructions.FROM(image, tag)
            }
            instruction.startsWith("RUN") -> {
                println("Enter new command for RUN instruction:")
                val command = scanner.nextLine()
                originalInstructions[lineNumber] = "RUN $command"
            }
            instruction.startsWith("COPY") -> {
                println("Enter new source and destination for COPY instruction (separated by a space):")
                val (source, destination) = scanner.nextLine().split(" ")
                originalInstructions[lineNumber] = "COPY $source $destination"
            }
            else -> {
                println("Unsupported instruction.")
                return
            }
        }

        file.writeText(originalInstructions.joinToString("\n"))
        println("Dockerfile updated.")
    }

    private fun generate(filePath: String) {
        val content = instructions.build()
        File(filePath).writeText(content)
    }
}
