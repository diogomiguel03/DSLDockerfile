// File: Add.kt
package org.example.DSL.Instructions

class Add(private val source: String, private val destination: String) : DockerInstruction {
    override fun generate() = "ADD $source $destination"
}
