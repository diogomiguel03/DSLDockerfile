// File: Shell.kt
package org.example.DSL.Instructions

class Shell(private val command: String) : DockerInstruction {
    override fun generate() = "SHELL $command"
}
