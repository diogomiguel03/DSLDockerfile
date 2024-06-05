// File: Entrypoint.kt
package org.example.DSL.Instructions

class Entrypoint(private val command: String) : DockerInstruction {
    override fun generate() = "ENTRYPOINT $command"
}
