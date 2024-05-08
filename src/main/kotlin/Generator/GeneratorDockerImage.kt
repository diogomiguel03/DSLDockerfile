package org.example.Generator

import java.io.File
import java.util.*

class GeneratorDockerImage {

    private val scanner = Scanner(System.`in`)

    fun buildImage() {
        println("Enter file path of Dockerfile to build:")
        val filePath = scanner.nextLine()
        val file = File(filePath)
        if (!file.exists()) {
            println("File does not exist.")
            return
        }

        println("Enter tag for Docker image (format: repository:tag):")
        val tag = scanner.nextLine()

        val process = ProcessBuilder("docker", "build", "-t", tag, "-f", filePath, ".").start()
        val exitCode = process.waitFor()
        if (exitCode == 0) {
            println("Docker image built successfully.")
        } else {
            println("Failed to build Docker image.")
        }
    }


}