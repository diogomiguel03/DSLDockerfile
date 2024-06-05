// File: Cmd.kt
package org.example.DSL.Instructions

class Cmd(private val command: String) : DockerInstruction {
    override fun generate() = "CMD $command"
}
