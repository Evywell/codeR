package fr.rob.gateway

import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.netty.NettyServer

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Gateway: Hello !")

            val server = Gateway()
            val socketBuilder = NettySessionSocketBuilder()
            val serverProcess = NettyServer(11111, server, socketBuilder, false)

            server.start(serverProcess)
        }
    }
}
