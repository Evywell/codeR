package fr.rob.gateway.extension

import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

interface ExtensionInterface {
    fun createDispatcher(gateway: Gateway): PacketDispatcherInterface
}
