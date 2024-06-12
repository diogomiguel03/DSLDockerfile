package org.example.Controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.example.Models.Metadata
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class DockerfileControllerTest {

    private lateinit var controller: DockerfileController
    private val tempDir = createTempDirectory().toFile()

    @BeforeEach
    fun setUp() {
        controller = DockerfileController()
    }

    @Test
    fun `test createCustomDockerfile`() {
        val dockerfilePath = File(tempDir, "Dockerfile").absolutePath
        val metadataPath = File(tempDir, "metadata-info.json").absolutePath

        controller.createCustomDockerfile("FROM", "python:3.10-slim", dockerfilePath)
        controller.createCustomDockerfile("WORKDIR", "/app", dockerfilePath)
        controller.createCustomDockerfile("COPY", "requirements.txt .", dockerfilePath)
        controller.createCustomDockerfile("RUN", "pip3 install -r requirements.txt", dockerfilePath)
        controller.createCustomDockerfile("COPY", ". .", dockerfilePath)
        controller.createCustomDockerfile("EXPOSE", "5000", dockerfilePath)
        controller.createCustomDockerfile("ENTRYPOINT", "[\"python3\", \"-m\", \"flask\", \"run\", \"--host=0.0.0.0\"]", dockerfilePath)

        // Generate the Dockerfile
        controller.createCustomDockerfile("", null, dockerfilePath)

        val dockerfile = File(dockerfilePath)
        assertTrue(dockerfile.exists(), "Dockerfile should be created")

        val expectedContent = """
            FROM python:3.10-slim:latest
            WORKDIR /app
            COPY requirements.txt .
            RUN pip3 install -r requirements.txt
            COPY . .
            EXPOSE 5000
            ENTRYPOINT ["python3", "-m", "flask", "run", "--host=0.0.0.0"]
        """.trimIndent()

        assertEquals(expectedContent, dockerfile.readText().trim(), "Dockerfile content should match expected content")

        val metadata = File(metadataPath)
        assertTrue(metadata.exists(), "Metadata file should be created")

        val objectMapper = jacksonObjectMapper()
        val metadataContent: Metadata = objectMapper.readValue(metadata)
        assertEquals("Python", metadataContent.dockerfileType, "Dockerfile type should be recognized as Python")
    }
}
