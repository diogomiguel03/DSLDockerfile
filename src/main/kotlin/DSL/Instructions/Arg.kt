// File: Arg.kt
package org.example.DSL.Instructions

class Arg(private val name: String, private val defaultValue: String?) : DockerInstruction {
    override fun generate() = if (defaultValue != null) "ARG $name=$defaultValue" else "ARG $name"
}
