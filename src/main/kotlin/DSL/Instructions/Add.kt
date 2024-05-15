package org.example.DSL.Instructions

class Add(private val source: String, private val destination: String) {
    fun generate() = "ADD $source $destination"
}
