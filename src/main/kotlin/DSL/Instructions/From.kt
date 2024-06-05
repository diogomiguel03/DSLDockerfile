// File: From.kt
package org.example.DSL.Instructions

class From(private val image: String, private val tag: String = "latest") : DockerInstruction {
    override fun generate() = "FROM $image:$tag"
}
