// File: InstructionsDockerfile.kt
package org.example.DSL

import org.example.DSL.Instructions.*

class InstructionsDockerfile {
    private val instructions = mutableListOf<DockerInstruction>()
    private var fromImage: String? = null

    fun from(image: String, tag: String? = "latest"): String {
        fromImage = image
        return addInstruction(From(image, tag ?: "latest"))
    }

    fun add(source: String, destination: String): String {
        return addInstruction(Add(source, destination))
    }

    fun arg(name: String, defaultValue: String? = null): String {
        return addInstruction(Arg(name, defaultValue))
    }

    fun cmd(command: String): String {
        return addInstruction(Cmd(command))
    }

    fun entrypoint(command: String): String {
        return addInstruction(Entrypoint(command))
    }

    fun env(key: String, value: String): String {
        return addInstruction(Env(key, value))
    }

    fun expose(port: Int): String {
        return addInstruction(Expose(port))
    }

    fun healthcheck(command: String): String {
        return addInstruction(Healthcheck(command))
    }

    fun label(key: String, value: String): String {
        return addInstruction(Label(key, value))
    }

    fun maintainer(name: String): String {
        return addInstruction(Maintainer(name))
    }

    fun run(command: String): String {
        return addInstruction(Run(command))
    }

    fun shell(command: String): String {
        return addInstruction(Shell(command))
    }

    fun stopsignal(signal: String): String {
        return addInstruction(Stopsignal(signal))
    }

    fun user(user: String): String {
        return addInstruction(User(user))
    }

    fun volume(volume: String): String {
        return addInstruction(Volume(volume))
    }

    fun workdir(path: String): String {
        return addInstruction(Workdir(path))
    }

    fun copy(source: String, destination: String): String {
        return addInstruction(Copy(source, destination))
    }

    private fun addInstruction(instruction: DockerInstruction): String {
        val instructionString = instruction.generate()
        instructions.add(instruction)
        return instructionString
    }

    fun build(): String {
        return instructions.joinToString("\n") { it.generate() }
    }

    fun getFromImage(): String? {
        return fromImage
    }

    fun getInstructions(): List<DockerInstruction> {
        return instructions
    }
}
