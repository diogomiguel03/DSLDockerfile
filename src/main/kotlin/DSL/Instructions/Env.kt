package org.example.DSL.Instructions

class Env(private val key: String, private val value: String) {
    fun generate() = "ENV $key=$value"
}
