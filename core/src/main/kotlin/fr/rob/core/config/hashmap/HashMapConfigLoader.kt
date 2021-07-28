package fr.rob.core.config.hashmap

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigLoaderInterface
import java.io.File

class HashMapConfigLoader : ConfigLoaderInterface {
    /**
     * This is a non-sense to have a file in parameter but, anyway...
     */
    override fun loadConfigFromFile(file: File): Config = HashMapConfig()
}
