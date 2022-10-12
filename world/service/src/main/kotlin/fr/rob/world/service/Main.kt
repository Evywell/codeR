package fr.rob.world.service

import fr.rob.world.service.infra.App
import fr.rob.world.service.infra.config.Config

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("World: Hello !")

            App(createConfigFromProperties()).run()
        }

        private fun createConfigFromProperties(): Config = Config(System.getProperty("world.grpc.port").toInt())
    }
}
