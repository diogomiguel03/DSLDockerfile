package org.example.DSL

import javafx.beans.property.SimpleStringProperty

class ContainerDSL {
    val name = SimpleStringProperty("")
    val imageName = SimpleStringProperty("")
    val imageTag = SimpleStringProperty("latest")
    val ports = SimpleStringProperty("")
    val envVars = SimpleStringProperty("")
    val volumes = SimpleStringProperty("")

    fun name(containerName: String) {
        name.set(containerName)
    }

    fun image(image: String) {
        imageName.set(image)
    }

    fun tag(tag: String) {
        imageTag.set(tag)
    }

    fun ports(ports: String) {
        this.ports.set(ports)
    }

    fun envVars(envVars: String) {
        this.envVars.set(envVars)
    }

    fun volumes(volumes: String) {
        this.volumes.set(volumes)
    }

    fun build(): ContainerDSL {
        return this
    }
}

fun container(init: ContainerDSL.() -> Unit): ContainerDSL {
    val dsl = ContainerDSL()
    dsl.init()
    return dsl.build()
}
