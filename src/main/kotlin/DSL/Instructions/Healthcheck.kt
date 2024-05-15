package org.example.DSL.Instructions

class Healthcheck(private val command: String) {
    fun generate() = "HEALTHCHECK CMD $command"
}
