package fr.rob.gateway.extension.eas

import fr.rob.gateway.extension.ExtensionInterface
import fr.rob.gateway.extension.eas.authentication.DevAuthenticationService
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class EasExtension : ExtensionInterface {
    override fun createDispatcher(gateway: Gateway): PacketDispatcherInterface {
        val easService = EasService(gateway, arrayOf(DevAuthenticationService()))

        return EasPacketDispatcher(easService)
    }
}
