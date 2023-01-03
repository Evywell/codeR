package fr.rob.gateway.extension.realm

import fr.raven.log.LoggerInterface
import fr.rob.gateway.extension.ExtensionInterface
import fr.rob.gateway.extension.game.GameNodeBuilder
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class RealmExtension(private val logger: LoggerInterface) : ExtensionInterface {
    override fun createDispatcher(gateway: Gateway): PacketDispatcherInterface {
        val realmService = RealmService(gateway, logger, GameNodeBuilder(gateway, logger))
        val realmClient = RealmClient(gateway.gameNodes, realmService)

        return RealmPacketDispatcher(realmClient, realmService, gateway.gameNodes)
    }
}
