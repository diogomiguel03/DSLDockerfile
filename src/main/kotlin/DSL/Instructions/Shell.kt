package org.example.DSL.Instructions

class Shell(private val command: String) {
    fun generate() = "SHELL $command"
}
