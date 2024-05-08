package org.example.DSL.Instructions

class Run(private val command: String) {
    fun generate() = "RUN $command"
}
