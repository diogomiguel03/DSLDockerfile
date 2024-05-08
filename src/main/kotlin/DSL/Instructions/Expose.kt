package org.example.DSL.Instructions

class Expose(private val port: Int) {
    fun generate() = "EXPOSE $port"
}
