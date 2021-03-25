package fr.rob.core.config.commons.configuration2

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigLoaderInterface
import java.io.File

class ConfigLoader : ConfigLoaderInterface {

    override fun loadConfigFromFile(file: File): Config = Config(file)
}
