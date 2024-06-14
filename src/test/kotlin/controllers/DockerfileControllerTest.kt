import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.example.Controllers.DockerfileController
import org.example.Models.Metadata
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class DockerfileControllerTest {

    private lateinit var controller: DockerfileController
    private lateinit var tempDir: File
    private lateinit var dockerfilePath: String

    @BeforeEach
    fun setUp() {
        controller = DockerfileController()
        tempDir = createTempDirectory().toFile()
        dockerfilePath = File(tempDir, "Dockerfile").absolutePath
    }

    @Test
    fun `test createDefaultDockerfile`() {
        controller.createDefaultDockerfile(
            image = "openjdk",
            tag = "11-jre-slim",
            updateCommand = "apt-get update",
            installCommand = "apt-get install -y maven",
            workdirPath = "/app",
            copySource = ".",
            copyDestination = "/app",
            compileCommand = "mvn clean install",
            cmdCommand = "java -jar app.jar",
            filePath = dockerfilePath
        )

        val dockerfile = File(dockerfilePath)
        assertTrue(dockerfile.exists(), "Dockerfile should be created")

        val expectedContent = """
            FROM openjdk:11-jre-slim
            RUN apt-get update
            RUN apt-get install -y maven
            WORKDIR /app
            COPY . /app
            RUN mvn clean install
            CMD java -jar app.jar
        """.trimIndent()

        assertEquals(expectedContent, dockerfile.readText().trim(), "Dockerfile content should match expected content")

        val metadataFile = File(tempDir, "metadata-info.json")
        assertTrue(metadataFile.exists(), "Metadata file should be created")

        val objectMapper = jacksonObjectMapper()
        val metadata: Metadata = objectMapper.readValue(metadataFile)
        assertEquals("Java", metadata.dockerfileType, "Dockerfile type should be recognized as Java")
    }

    @Test
    fun `test createCustomDockerfile`() {
        controller.createCustomDockerfile("FROM", "python:3.10-slim", dockerfilePath)
        controller.createCustomDockerfile("WORKDIR", "/app", dockerfilePath)
        controller.createCustomDockerfile("COPY", "requirements.txt .", dockerfilePath)
        controller.createCustomDockerfile("RUN", "pip3 install -r requirements.txt", dockerfilePath)
        controller.createCustomDockerfile("COPY", ". .", dockerfilePath)
        controller.createCustomDockerfile("EXPOSE", "5000", dockerfilePath)
        controller.createCustomDockerfile("ENTRYPOINT", "[\"python3\", \"-m\", \"flask\", \"run\", \"--host=0.0.0.0\"]", dockerfilePath)
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

        val metadataFile = File(tempDir, "metadata-info.json")
        assertTrue(metadataFile.exists(), "Metadata file should be created")

        val objectMapper = jacksonObjectMapper()
        val metadata: Metadata = objectMapper.readValue(metadataFile)
        assertEquals("Python", metadata.dockerfileType, "Dockerfile type should be recognized as Python")
    }

    @Test
    fun `test loadDockerfile`() {
        val content = "FROM openjdk:11-jre-slim"
        File(dockerfilePath).writeText(content)

        val loadedContent = controller.loadDockerfile(File(dockerfilePath))
        assertEquals(content, loadedContent, "Loaded Dockerfile content should match")
    }

    @Test
    fun `test saveDockerfile`() {
        val content = "FROM openjdk:11-jre-slim"
        controller.saveDockerfile(dockerfilePath, content)

        val savedContent = File(dockerfilePath).readText()
        assertEquals(content, savedContent, "Saved Dockerfile content should match")
    }

    @Test
    fun `test resetInstructions`() {
        controller.createCustomDockerfile("FROM", "python:3.10-slim")
        controller.resetInstructions()
        controller.createCustomDockerfile("", null, dockerfilePath)

        val dockerfile = File(dockerfilePath)
        assertTrue(dockerfile.exists(), "Dockerfile should be created")
        assertEquals("", dockerfile.readText().trim(), "Dockerfile content should be empty after reset")
    }
}
