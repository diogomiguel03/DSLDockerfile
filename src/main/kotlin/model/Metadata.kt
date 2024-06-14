// File: Metadata.kt
package org.example.Models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Metadata @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("timestamp") val timestamp: String,
    @JsonProperty("dockerfilePath") val dockerfilePath: String,
    @JsonProperty("imageNames") val imageNames: MutableList<String> = mutableListOf(),
    @JsonProperty("containers") val containers: MutableList<String> = mutableListOf(),
    @JsonProperty("dockerfileType") val dockerfileType: String? = null
)
