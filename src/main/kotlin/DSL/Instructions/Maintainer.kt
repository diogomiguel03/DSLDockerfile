// File: Maintainer.kt
package org.example.DSL.Instructions

class Maintainer(private val name: String) : DockerInstruction {
    override fun generate() = "MAINTAINER $name"
}
