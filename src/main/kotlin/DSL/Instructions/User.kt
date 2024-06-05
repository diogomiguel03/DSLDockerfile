// File: User.kt
package org.example.DSL.Instructions

class User(private val user: String) : DockerInstruction {
    override fun generate() = "USER $user"
}
