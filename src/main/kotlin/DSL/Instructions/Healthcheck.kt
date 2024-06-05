// File: Healthcheck.kt
package org.example.DSL.Instructions

class Healthcheck(private val command: String) : DockerInstruction {
    override fun generate() = "HEALTHCHECK $command"
}
