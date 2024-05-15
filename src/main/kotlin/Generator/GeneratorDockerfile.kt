package org.example.generator

import org.example.DSL.InstructionsDockerfile
import java.io.File
import java.util.*

class GeneratorDockerfile(private val instructions: InstructionsDockerfile) {
    private val scanner = Scanner(System.`in`)

    fun createDefaultDockerfile() {
        instructions.apply {
            from("ubuntu", "latest")
            run("apt-get update")
            run("apt-get install -y openjdk-11-jdk")
            workdir("/app")
            copy(".", "/app")
            run("javac Main.java")
            cmd("java Main")
        }

        val filePath = readInput("Enter file path for Dockerfile (leave blank for './Dockerfile'):", "./Dockerfile")
        generate(filePath)
        println("Dockerfile created at $filePath")
    }

    fun createCustomDockerfile() {
        while (true) {
            println("Select an instruction to add:")
            println("1. FROM")
            println("2. ADD")
            println("3. ARG")
            println("4. CMD")
            println("5. ENTRYPOINT")
            println("6. ENV")
            println("7. EXPOSE")
            println("8. HEALTHCHECK")
            println("9. LABEL")
            println("10. MAINTAINER")
            println("11. RUN")
            println("12. SHELL")
            println("13. STOPSIGNAL")
            println("14. USER")
            println("15. VOLUME")
            println("16. WORKDIR")
            println("17. COPY")
            println("18. Finish and create Dockerfile")

            when (scanner.nextLine()) {
                "1" -> addFrom()
                "2" -> addAdd()
                "3" -> addArg()
                "4" -> addCmd()
                "5" -> addEntrypoint()
                "6" -> addEnv()
                "7" -> addExpose()
                "8" -> addHealthcheck()
                "9" -> addLabel()
                "10" -> addMaintainer()
                "11" -> addRun()
                "12" -> addShell()
                "13" -> addStopsignal()
                "14" -> addUser()
                "15" -> addVolume()
                "16" -> addWorkdir()
                "17" -> addCopy()
                "18" -> break
                else -> println("Invalid option")
            }
        }

        val filePath = readInput("Enter file path for Dockerfile (leave blank for './Dockerfile'):", "./Dockerfile")
        generate(filePath)
        println("Dockerfile created at $filePath")
    }

    private fun addFrom() {
        val image = readInput("Enter image for FROM instruction:")
        val tag = readInput("Enter tag for FROM instruction (leave blank for 'latest'):", "latest")
        instructions.from(image, tag)
    }

    private fun addAdd() {
        val source = readInput("Enter source for ADD instruction:")
        val destination = readInput("Enter destination for ADD instruction:")
        instructions.add(source, destination)
    }

    private fun addArg() {
        val name = readInput("Enter name for ARG instruction:")
        val defaultValue = readInput("Enter default value for ARG instruction (leave blank if none):")
        instructions.arg(name, if (defaultValue.isBlank()) null else defaultValue)
    }

    private fun addCmd() {
        val command = readInput("Enter command for CMD instruction:")
        instructions.cmd(command)
    }

    private fun addEntrypoint() {
        val command = readInput("Enter command for ENTRYPOINT instruction:")
        instructions.entrypoint(command)
    }

    private fun addEnv() {
        val key = readInput("Enter key for ENV instruction:")
        val value = readInput("Enter value for ENV instruction:")
        instructions.env(key, value)
    }

    private fun addExpose() {
        val port = readInput("Enter port for EXPOSE instruction:").toInt()
        instructions.expose(port)
    }

    private fun addHealthcheck() {
        val command = readInput("Enter command for HEALTHCHECK instruction:")
        instructions.healthcheck(command)
    }

    private fun addLabel() {
        val key = readInput("Enter key for LABEL instruction:")
        val value = readInput("Enter value for LABEL instruction:")
        instructions.label(key, value)
    }

    private fun addMaintainer() {
        val name = readInput("Enter name for MAINTAINER instruction:")
        instructions.maintainer(name)
    }

    private fun addRun() {
        val command = readInput("Enter command for RUN instruction:")
        instructions.run(command)
    }

    private fun addShell() {
        val command = readInput("Enter command for SHELL instruction:")
        instructions.shell(command)
    }

    private fun addStopsignal() {
        val signal = readInput("Enter signal for STOPSIGNAL instruction:")
        instructions.stopsignal(signal)
    }

    private fun addUser() {
        val user = readInput("Enter user for USER instruction:")
        instructions.user(user)
    }

    private fun addVolume() {
        val volume = readInput("Enter volume for VOLUME instruction:")
        instructions.volume(volume)
    }

    private fun addWorkdir() {
        val path = readInput("Enter path for WORKDIR instruction:")
        instructions.workdir(path)
    }

    private fun addCopy() {
        val source = readInput("Enter source for COPY instruction:")
        val destination = readInput("Enter destination for COPY instruction:")
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
