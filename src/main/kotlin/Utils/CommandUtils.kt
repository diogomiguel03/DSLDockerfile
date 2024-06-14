package org.example.Utils

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader

private val logger = KotlinLogging.logger {}

object CommandUtils {
    fun runCommand(args: List<String>): Pair<Boolean, List<String>> {
        val processBuilder = ProcessBuilder(args)
        return try {
            val process = processBuilder.start()
            val output = mutableListOf<String>()
            val errorOutput = mutableListOf<String>()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.lines().forEach { output.add(it) }
            }
            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                reader.lines().forEach { errorOutput.add(it) }
            }
            val exitCode = process.waitFor()
            val success = exitCode == 0
            if (!success) {
                output.addAll(errorOutput)
            }
            Pair(success, output)
        } catch (e: Exception) {
            logger.error(e) { "Command execution failed: ${args.joinToString(" ")}" }
            Pair(false, listOf("Error: ${e.message}"))
        }
    }
}
