package fr.rob.login.test.feature.service.config

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigLoaderInterface
import java.io.File

class ConfigLoader: ConfigLoaderInterface {

    override fun loadConfigFromFile(file: File): Config  = fr.rob.login.test.feature.service.config.Config()
}
