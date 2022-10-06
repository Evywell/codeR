package fr.rob.gateway

import fr.raven.log.log4j.LoggerFactory
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.netty.NettyServer
import java.io.File

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Gateway: Hello !")

            val logger = LoggerFactory(File({}.javaClass.classLoader.getResource("log4j.config.xml")!!.path))
                .create("GATEWAY")
            val server = Gateway(logger)
            val socketBuilder = NettySessionSocketBuilder()
            val serverProcess = NettyServer(11111, server, socketBuilder, false)

            server.start(serverProcess)
        }
    }
}
