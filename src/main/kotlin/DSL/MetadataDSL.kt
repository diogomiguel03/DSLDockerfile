package org.example.DSL

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

data class Metadata @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("timestamp") val timestamp: String,
    @JsonProperty("dockerfilePath") val dockerfilePath: String,
    @JsonProperty("imageNames") val imageNames: MutableList<String> = mutableListOf(),
    @JsonProperty("containers") val containers: MutableList<String> = mutableListOf(),
    @JsonProperty("dockerfileType") val dockerfileType: String? = null
)

class MetadataDSL {
    var name: String = ""
    var timestamp: String = ""
    var dockerfilePath: String = ""
    var dockerfileType: String? = null
    val imageNames: MutableList<String> = mutableListOf()
    val containers: MutableList<String> = mutableListOf()

    fun imageName(name: String) {
        imageNames.add(name)
    }

    fun container(name: String) {
        containers.add(name)
    }

    fun build(): Metadata {
        return Metadata(name, timestamp, dockerfilePath, imageNames, containers, dockerfileType)
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()

        fun saveToFile(filePath: String, metadata: Metadata) {
            val file = File(filePath)
            objectMapper.writeValue(file, metadata)
        }

        fun parseFromFile(file: File): Metadata {
            return objectMapper.readValue(file, Metadata::class.java)
        }

        fun findMetadataFiles(directory: File): List<File> {
            return directory.walk()
                .filter { it.isFile && it.name == "metadata-info.json" }
                .toList()
        }
    }
}

fun metadata(init: MetadataDSL.() -> Unit): Metadata {
    val dsl = MetadataDSL()
    dsl.init()
    return dsl.build()
}
