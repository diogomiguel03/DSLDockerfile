package org.example.model

import org.example.DSL.InstructionsDockerfile

data class Dockerfile(
    val name: String,
    val path: String,
    val instructions: List<InstructionsDockerfile>
)
