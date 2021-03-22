package fr.rob.code.config

import java.io.File

interface ConfigLoaderInterface {

    fun loadConfigFromFile(file: File): Config
}
