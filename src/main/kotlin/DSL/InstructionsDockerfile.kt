package org.example.DSL

import org.example.DSL.Instructions.*

class InstructionsDockerfile {
    val instructions = mutableListOf<String>()

    fun FROM(image: String, tag: String = "latest") : String {
        val from = From(image, tag)
        val instruction = from.generate()
        instructions.add(from.generate())
        return instruction
    }

    fun ENTRYPOINT(command: String): String {
        val entrypoint = Entrypoint(command)
        val instruction = entrypoint.generate()
        instructions.add(instruction)
        return instruction
    }

    fun EXPOSE(port: Int): String {
        val expose = Expose(port)
        val instruction = expose.generate()
        instructions.add(instruction)
        return instruction
    }

    fun RUN(command: String): String {
        val run = Run(command)
        val instruction = run.generate()
        instructions.add(instruction)
        return instruction
    }

    fun WORKDIR(path: String): String {
        val workdir = Workdir(path)
        val instruction = workdir.generate()
        instructions.add(instruction)
        return instruction
    }

    fun COPY(source: String, destination: String): String {
        val copy = Copy(source, destination)
        val instruction = copy.generate()
        instructions.add(instruction)
        return instruction
    }

    fun build() = instructions.joinToString("\n")
}
