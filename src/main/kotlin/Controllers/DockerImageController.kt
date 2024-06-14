package org.example.Controllers

import javafx.beans.property.SimpleStringProperty
import org.example.DSL.dockerImage

class DockerImageController {
    val dockerfilePath = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")
    val outputLog = SimpleStringProperty("")

    fun createDockerImage(onComplete: (Boolean) -> Unit) {
        val dockerfilePathValue = dockerfilePath.get()
        val imageNameValue = imageName.get()
        val imageTagValue = imageTag.get()

        if (dockerfilePathValue.isNullOrEmpty() || imageNameValue.isNullOrEmpty()) {
            outputLog.set("Dockerfile path and image name must not be null or empty.")
            onComplete(false)
            return
        }

        dockerImage({
            from(dockerfilePathValue)
            name(imageNameValue)
            tag(imageTagValue)
        }, onComplete)
    }
}
