package org.example.Controllers

import org.example.DSL.DockerfileDSL
import org.example.DSL.MetadataDSL
import org.example.DSL.dockerfile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class DockerfileControllerTest {

    @Test
    fun `test createDefaultDockerfile`() {
        val controller = DockerfileController()
        val filePath = "test_Dockerfile"
        controller.createDefaultDockerfile(
            "ubuntu", "latest", "apt-get update", "apt-get install -y curl",
            "/app", ".", "/app", "make", "make test", filePath
        )
        val dockerfile = File(filePath)
        assertTrue(dockerfile.exists())
        val content = dockerfile.readText()
        assertTrue(content.contains("FROM ubuntu:latest"))
        assertTrue(content.contains("RUN apt-get update"))
        assertTrue(content.contains("RUN apt-get install -y curl"))
        assertTrue(content.contains("WORKDIR /app"))
        assertTrue(content.contains("COPY . /app"))
        assertTrue(content.contains("RUN make"))
        assertTrue(content.contains("CMD make test"))
    }

    @Test
    fun `test createCustomDockerfile`() {
        val controller = DockerfileController()
        controller.createCustomDockerfile("FROM", "ubuntu:latest")
        controller.createCustomDockerfile("RUN", "apt-get update")
        controller.createCustomDockerfile("RUN", "apt-get install -y curl")
        controller.createCustomDockerfile("WORKDIR", "/app")
        controller.createCustomDockerfile("COPY", ". /app")
        controller.createCustomDockerfile("RUN", "make")
        controller.createCustomDockerfile("CMD", "make test")
        val filePath = "custom_Dockerfile"
        controller.createCustomDockerfile("", null, filePath)
        val dockerfile = File(filePath)
        assertTrue(dockerfile.exists())
        val content = dockerfile.readText()
        assertTrue(content.contains("FROM ubuntu:latest"))
        assertTrue(content.contains("RUN apt-get update"))
        assertTrue(content.contains("RUN apt-get install -y curl"))
        assertTrue(content.contains("WORKDIR /app"))
        assertTrue(content.contains("COPY . /app"))
        assertTrue(content.contains("RUN make"))
        assertTrue(content.contains("CMD make test"))
    }

    @Test
    fun `test determineDockerfileType`() {
        val controller = DockerfileController()
        assertEquals("Python", controller.determineDockerfileType("python:3.8"))
        assertEquals("Node.js", controller.determineDockerfileType("node:14"))
        assertEquals("Java", controller.determineDockerfileType("openjdk:11"))
        assertEquals("Go", controller.determineDockerfileType("golang:1.16"))
        assertEquals("Unknown", controller.determineDockerfileType("unknownimage:latest"))
    }

    @Test
    fun `test createMetadataFile`() {
        val controller = DockerfileController()
        val filePath = "test_metadata.json"
        controller.createMetadataFile("testDockerfile", filePath, "Python")
        val metadataFile = File(File(filePath).parent, "metadata-info.json")
        assertTrue(metadataFile.exists())
        val metadata = MetadataDSL.parseFromFile(metadataFile)
        assertEquals("testDockerfile", metadata.name)
        assertEquals("Python", metadata.dockerfileType)
    }
}
