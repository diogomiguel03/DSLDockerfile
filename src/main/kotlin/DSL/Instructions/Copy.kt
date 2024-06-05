// File: Copy.kt
package org.example.DSL.Instructions

class Copy(private val source: String, private val destination: String) : DockerInstruction {
    override fun generate() = "COPY $source $destination"
}
