// File: DockerComposeService.kt
package org.example.DSL

class DockerComposeService(val name: String) {
    var image: String = ""
    val ports = mutableListOf<String>()
    val environment = mutableMapOf<String, String>()
    val volumes = mutableListOf<String>()

    fun build(): String {
        return buildString {
            append("  $name:\n")
            append("    image: $image\n")
            if (ports.isNotEmpty()) {
                append("    ports:\n")
                ports.forEach { append("      - \"$it\"\n") }
            }
            if (environment.isNotEmpty()) {
                append("    environment:\n")
                environment.forEach { (key, value) -> append("      - $key=$value\n") }
            }
            if (volumes.isNotEmpty()) {
                append("    volumes:\n")
                volumes.forEach { append("      - \"$it\"\n") }
            }
        }
    }
}
