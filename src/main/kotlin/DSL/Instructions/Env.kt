// File: Env.kt
package org.example.DSL.Instructions

class Env(private val key: String, private val value: String) : DockerInstruction {
    override fun generate() = "ENV $key=$value"
}
