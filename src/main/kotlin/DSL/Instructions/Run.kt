// File: Run.kt
package org.example.DSL.Instructions

class Run(private val command: String) : DockerInstruction {
    override fun generate() = "RUN $command"
}
