package fr.rob.core.config

import java.io.File

interface ConfigLoaderInterface {

    fun loadConfigFromFile(file: File): Config
}
