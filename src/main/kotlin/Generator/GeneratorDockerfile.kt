package org.example.Generator

import org.example.DSL.InstructionsDockerfile
import java.io.File
import java.util.*

class GeneratorDockerfile(private val instructions: InstructionsDockerfile) {
    private val scanner = Scanner(System.`in`)

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

    fun createCustomDockerfile(instruction: String, parameters: String, filePath: String) {
        when (instruction) {
            "FROM" -> {
                val (image, tag) = parameters.split(" ")
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
        }
        generate(filePath)
    }


    private fun addFrom(image: String, tag: String = "latest") {
        instructions.from(image, tag)
    }

    private fun addAdd(source: String, destination: String) {
        instructions.add(source, destination)
    }

    private fun addArg(name: String, defaultValue: String? = null) {
        instructions.arg(name, defaultValue)
    }

    private fun addCmd(command: String) {
        instructions.cmd(command)
    }

    private fun addEntrypoint(command: String) {
        instructions.entrypoint(command)
    }

    private fun addEnv(key: String, value: String) {
        instructions.env(key, value)
    }

    private fun addExpose(port: Int) {
        instructions.expose(port)
    }

    private fun addHealthcheck(command: String) {
        instructions.healthcheck(command)
    }

    private fun addLabel(key: String, value: String) {
        instructions.label(key, value)
    }

    private fun addMaintainer(name: String) {
        instructions.maintainer(name)
    }

    private fun addRun(command: String) {
        instructions.run(command)
    }

    private fun addShell(command: String) {
        instructions.shell(command)
    }

    private fun addStopsignal(signal: String) {
        instructions.stopsignal(signal)
    }

    private fun addUser(user: String) {
        instructions.user(user)
    }

    private fun addVolume(volume: String) {
        instructions.volume(volume)
    }

    private fun addWorkdir(path: String) {
        instructions.workdir(path)
    }

    private fun addCopy(source: String, destination: String) {
        instructions.copy(source, destination)
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
}
