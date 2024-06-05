// File: Stopsignal.kt
package org.example.DSL.Instructions

class Stopsignal(private val signal: String) : DockerInstruction {
    override fun generate() = "STOPSIGNAL $signal"
}
