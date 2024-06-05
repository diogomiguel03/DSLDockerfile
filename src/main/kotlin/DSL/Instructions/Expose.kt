// File: Expose.kt
package org.example.DSL.Instructions

class Expose(private val port: Int) : DockerInstruction {
    override fun generate() = "EXPOSE $port"
}
