package org.example.DSL

import javafx.beans.property.SimpleStringProperty

class DockerImageDSL {
    val dockerfilePath = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")

    fun from(path: String) {
        dockerfilePath.set(path)
    }

    fun name(name: String) {
        imageName.set(name)
    }

    fun tag(tag: String) {
        imageTag.set(tag)
    }

    fun build(): DockerImageDSL {
        return this
    }
}

fun dockerImage(init: DockerImageDSL.() -> Unit): DockerImageDSL {
    val dsl = DockerImageDSL()
    dsl.init()
    return dsl.build()
}
