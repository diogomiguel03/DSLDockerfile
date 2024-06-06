// File: Config.kt
package org.example

import com.typesafe.config.ConfigFactory

object Config {
    private val config = ConfigFactory.load()

    val defaultTag: String = config.getString("docker.defaultTag")
}
