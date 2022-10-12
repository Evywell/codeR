package fr.rob.gateway.network

import fr.rob.gateway.extension.ExtensionInterface

class GatewayBuilder {
    fun withExtensions(vararg extensions: ExtensionInterface): Gateway {
        val gateway = Gateway()

        for (extension in extensions) {
            gateway.registerExtension(extension)
        }

        return gateway
    }
}
