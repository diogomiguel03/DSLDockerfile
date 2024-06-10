// File: Metadata.kt
package org.example.Models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Metadata @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("timestamp") val timestamp: String,
    @JsonProperty("imageName") var imageName: String? = null,
    @JsonProperty("dockerfilePath") val dockerfilePath: String
)
