package org.example.DSL.Instructions

class From(image: String, tag: String = "latest") {

    val imageWithTag = "$image:$tag"

    fun generate() = "FROM $imageWithTag"
}
