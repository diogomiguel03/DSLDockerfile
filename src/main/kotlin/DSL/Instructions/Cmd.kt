package org.example.DSL.Instructions

class Cmd(private val command: String) {
    fun generate() = "CMD $command"
}
