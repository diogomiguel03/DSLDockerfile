// File: Label.kt
package org.example.DSL.Instructions

class Label(private val key: String, private val value: String) : DockerInstruction {
    override fun generate() = "LABEL $key=$value"
}
