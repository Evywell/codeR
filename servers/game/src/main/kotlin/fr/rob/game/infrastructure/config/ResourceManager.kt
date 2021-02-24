package fr.rob.game.infrastructure.config

import fr.rob.game.Main
import java.io.InputStream
import java.net.URL

object ResourceManager {

    fun getResourceURL(resourcePath: String): URL? = Main::class.java.classLoader.getResource(resourcePath)

    fun getResourceStream(resourcePath: String): InputStream? {
        return Main::class.java.classLoader.getResourceAsStream(resourcePath)
    }
}
