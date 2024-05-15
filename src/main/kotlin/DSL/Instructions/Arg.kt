package org.example.DSL.Instructions

class Arg(private val name: String, private val defaultValue: String? = null) {
    fun generate() = if (defaultValue != null) "ARG $name=$defaultValue" else "ARG $name"
}
