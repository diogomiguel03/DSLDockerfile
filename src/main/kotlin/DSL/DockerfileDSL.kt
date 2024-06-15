package org.example.DSL

import org.example.DSL.Instructions.*
import java.io.File

class DockerfileDSL {
    private val instructions = mutableListOf<DockerInstruction>()
    private var fromImage: String? = null

    fun from(image: String, tag: String = "latest"): DockerfileDSL {
        fromImage = image
        addInstruction(From(image, tag))
        return this
    }

    fun add(source: String, destination: String): DockerfileDSL {
        addInstruction(Add(source, destination))
        return this
    }

    fun arg(name: String, defaultValue: String? = null): DockerfileDSL {
        addInstruction(Arg(name, defaultValue))
        return this
    }

    fun cmd(command: String): DockerfileDSL {
        addInstruction(Cmd(command))
        return this
    }

    fun entrypoint(command: String): DockerfileDSL {
        addInstruction(Entrypoint(command))
        return this
    }

    fun env(key: String, value: String): DockerfileDSL {
        addInstruction(Env(key, value))
        return this
    }

    fun expose(port: Int): DockerfileDSL {
        addInstruction(Expose(port))
        return this
    }

    fun healthcheck(command: String): DockerfileDSL {
        addInstruction(Healthcheck(command))
        return this
    }

    fun label(key: String, value: String): DockerfileDSL {
        addInstruction(Label(key, value))
        return this
    }

    fun maintainer(name: String): DockerfileDSL {
        addInstruction(Maintainer(name))
        return this
    }

    fun run(command: String): DockerfileDSL {
        addInstruction(Run(command))
        return this
    }

    fun shell(command: String): DockerfileDSL {
        addInstruction(Shell(command))
        return this
    }

    fun stopsignal(signal: String): DockerfileDSL {
        addInstruction(Stopsignal(signal))
        return this
    }

    fun user(user: String): DockerfileDSL {
        addInstruction(User(user))
        return this
    }

    fun volume(volume: String): DockerfileDSL {
        addInstruction(Volume(volume))
        return this
    }

    fun workdir(path: String): DockerfileDSL {
        addInstruction(Workdir(path))
        return this
    }

    fun copy(source: String, destination: String): DockerfileDSL {
        addInstruction(Copy(source, destination))
        return this
    }

    private fun addInstruction(instruction: DockerInstruction) {
        instructions.add(instruction)
    }

    fun build(): String {
        return instructions.joinToString("\n") { it.generate() }
    }

    fun getFromImage(): String? {
        return fromImage
    }

    fun saveToFile(filePath: String) {
        File(filePath).writeText(build())
    }

}

fun dockerfile(init: DockerfileDSL.() -> Unit): DockerfileDSL {
    val dsl = DockerfileDSL()
    dsl.init()
    return dsl
}
