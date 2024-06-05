// File: Volume.kt
package org.example.DSL.Instructions

class Volume(private val volume: String) : DockerInstruction {
    override fun generate() = "VOLUME $volume"
}
