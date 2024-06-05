// File: Workdir.kt
package org.example.DSL.Instructions

class Workdir(private val path: String) : DockerInstruction {
    override fun generate() = "WORKDIR $path"
}
