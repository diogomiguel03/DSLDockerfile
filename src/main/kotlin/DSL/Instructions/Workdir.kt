package org.example.DSL.Instructions

class Workdir(private val path: String) {
    fun generate() = "WORKDIR $path"
}
