package fr.rob.code.config.commons.configuration2

import fr.rob.code.config.Config
import fr.rob.code.config.ConfigLoaderInterface
import java.io.File

class ConfigLoader : ConfigLoaderInterface {

    override fun loadConfigFromFile(file: File): Config = Config(file)
}
