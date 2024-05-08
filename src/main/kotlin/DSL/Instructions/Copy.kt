package org.example.DSL.Instructions

class Copy(private val source: String, private val destination: String) {
    fun generate() = "COPY $source $destination"
}
