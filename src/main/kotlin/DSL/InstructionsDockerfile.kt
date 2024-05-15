package org.example.DSL

import org.example.DSL.Instructions.*

class InstructionsDockerfile {
    val instructions = mutableListOf<String>()

    fun from(image: String, tag: String = "latest"): String {
        val from = From(image, tag)
        val instruction = from.generate()
        instructions.add(instruction)
        return instruction
    }

    fun add(source: String, destination: String): String {
        val add = Add(source, destination)
        val instruction = add.generate()
        instructions.add(instruction)
        return instruction
    }

    fun arg(name: String, defaultValue: String? = null): String {
        val arg = Arg(name, defaultValue)
        val instruction = arg.generate()
        instructions.add(instruction)
        return instruction
    }

    fun cmd(command: String): String {
        val cmd = Cmd(command)
        val instruction = cmd.generate()
        instructions.add(instruction)
        return instruction
    }

    fun entrypoint(command: String): String {
        val entrypoint = Entrypoint(command)
        val instruction = entrypoint.generate()
        instructions.add(instruction)
        return instruction
    }

    fun env(key: String, value: String): String {
        val env = Env(key, value)
        val instruction = env.generate()
        instructions.add(instruction)
        return instruction
    }

    fun expose(port: Int): String {
        val expose = Expose(port)
        val instruction = expose.generate()
        instructions.add(instruction)
        return instruction
    }

    fun healthcheck(command: String): String {
        val healthcheck = Healthcheck(command)
        val instruction = healthcheck.generate()
        instructions.add(instruction)
        return instruction
    }

    fun label(key: String, value: String): String {
        val label = Label(key, value)
        val instruction = label.generate()
        instructions.add(instruction)
        return instruction
    }

    fun maintainer(name: String): String {
        val maintainer = Maintainer(name)
        val instruction = maintainer.generate()
        instructions.add(instruction)
        return instruction
    }

    fun run(command: String): String {
        val run = Run(command)
        val instruction = run.generate()
        instructions.add(instruction)
        return instruction
    }

    fun shell(command: String): String {
        val shell = Shell(command)
        val instruction = shell.generate()
        instructions.add(instruction)
        return instruction
    }

    fun stopsignal(signal: String): String {
        val stopsignal = Stopsignal(signal)
        val instruction = stopsignal.generate()
        instructions.add(instruction)
        return instruction
    }

    fun user(user: String): String {
        val user = User(user)
        val instruction = user.generate()
        instructions.add(instruction)
        return instruction
    }

    fun volume(volume: String): String {
        val volume = Volume(volume)
        val instruction = volume.generate()
        instructions.add(instruction)
        return instruction
    }

    fun workdir(path: String): String {
        val workdir = Workdir(path)
        val instruction = workdir.generate()
        instructions.add(instruction)
        return instruction
    }

    fun copy(source: String, destination: String): String {
        val copy = Copy(source, destination)
        val instruction = copy.generate()
        instructions.add(instruction)
        return instruction
    }

    fun build() = instructions.joinToString("\n")
}
