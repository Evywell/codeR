package fr.rob.gateway.extension.game

import fr.raven.log.LoggerInterface
import fr.rob.gateway.extension.ExtensionInterface
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class GameExtension(private val logger: LoggerInterface) : ExtensionInterface {
    override fun createDispatcher(gateway: Gateway): PacketDispatcherInterface =
        GameNodePacketDispatcher(GameNodePacketBuilder(), logger)
}
