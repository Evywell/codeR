package fr.rob.gateway

import fr.raven.log.log4j.LoggerFactory
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
import fr.rob.gateway.extension.eas.EasExtension
import fr.rob.gateway.extension.game.GameExtension
import fr.rob.gateway.extension.realm.RealmExtension
import fr.rob.gateway.network.GatewayBuilder
import fr.rob.gateway.network.netty.NettyServer
import java.io.File

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Gateway: Hello !")

            val logConfigResource = requireNotNull({}.javaClass.classLoader.getResource("log4j.config.xml")) {
                "log4j.config.xml not found in classpath"
            }
            val loggerFactory = LoggerFactory(File(logConfigResource.path).inputStream())
            val logger = loggerFactory
                .create("GATEWAY")

            val gateway = GatewayBuilder()
                .withExtensions(
                    EasExtension(),
                    RealmExtension(logger, loggerFactory),
                    GameExtension(logger)
                )

            val socketBuilder = NettySessionSocketBuilder()
            val serverProcess = NettyServer(11111, gateway, socketBuilder, false)

            gateway.start(serverProcess)
        }
    }
}
