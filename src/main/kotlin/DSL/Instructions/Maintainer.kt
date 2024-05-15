package org.example.DSL.Instructions

class Maintainer(private val name: String) {
    fun generate() = "MAINTAINER $name"
}
