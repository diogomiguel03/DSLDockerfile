// File: DockerComposeFile.kt
package org.example.DSL

class DockerComposeFile {
    val services = mutableListOf<DockerComposeService>()

    fun addService(service: DockerComposeService) {
        services.add(service)
    }

    fun build(): String {
        return buildString {
            append("version: '3'\n")
            append("services:\n")
            services.forEach { append(it.build()) }
        }
    }
}
