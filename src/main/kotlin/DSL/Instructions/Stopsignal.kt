package org.example.DSL.Instructions

class Stopsignal(private val signal: String) {
    fun generate() = "STOPSIGNAL $signal"
}
