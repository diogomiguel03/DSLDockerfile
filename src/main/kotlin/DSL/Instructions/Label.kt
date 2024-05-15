package org.example.DSL.Instructions

class Label(private val key: String, private val value: String) {
    fun generate() = "LABEL $key=$value"
}
