package org.example.DSL.Instructions

class Volume(private val volume: String) {
    fun generate() = "VOLUME $volume"
}
