package org.example.Generator

import java.io.File

class GeneratorDockerImage {

    fun createDockerImage(dockerfilePath: String, imageName: String): Boolean {
        try {
            val dockerfile = File(dockerfilePath)
            if (!dockerfile.exists()) {
                println("Dockerfile does not exist at: $dockerfilePath")
                return false
            }

            // Get the parent directory of the Dockerfile to use as the build context
            val dockerDirectory = dockerfile.parentFile
            if (dockerDirectory == null || !dockerDirectory.exists()) {
                println("Docker directory does not exist: ${dockerDirectory?.absolutePath}")
                return false
            }

            val processBuilder = ProcessBuilder(
                "docker", "build", "-t", imageName, "-f", dockerfilePath, dockerDirectory.absolutePath
            )
            processBuilder.directory(dockerDirectory)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val reader = process.inputStream.bufferedReader()
            reader.lines().forEach { println(it) }

            val exitCode = process.waitFor()
            if (exitCode == 0) {
                println("Docker image created successfully with name: $imageName")
                return true
            } else {
                println("Failed to create Docker image. Exit code: $exitCode")
                return false
            }
        } catch (e: Exception) {
            println("An error occurred while creating the Docker image: ${e.message}")
            return false
        }
    }
}
