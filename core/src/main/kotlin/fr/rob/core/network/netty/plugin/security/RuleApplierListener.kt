package fr.rob.core.network.netty.plugin.security

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.core.network.netty.NettyPlugin

class RuleApplierListener(private val plugin: NettyPlugin) : EventListenerInterface {

    override fun process(event: EventInterface) {
        plugin.applyRules(event)
    }
}
