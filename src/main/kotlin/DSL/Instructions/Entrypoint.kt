package org.example.DSL.Instructions

class Entrypoint(private val command: String) {
    fun generate() = "ENTRYPOINT $command"
}
