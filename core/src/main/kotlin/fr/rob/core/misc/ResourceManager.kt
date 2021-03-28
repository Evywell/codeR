package fr.rob.core.misc

import java.io.InputStream
import java.net.URL

object ResourceManager {

    fun getResourceURL(resourcePath: String): URL? = ResourceManager::class.java.classLoader.getResource(resourcePath)

    fun getResourceStream(resourcePath: String): InputStream? {
        return ResourceManager::class.java.classLoader.getResourceAsStream(resourcePath)
    }
}
