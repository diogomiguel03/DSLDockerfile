package org.example.DSL.Instructions

class User(private val user: String) {
    fun generate() = "USER $user"
}
