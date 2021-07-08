package fr.rob.shared.network.netty.security.rule

import fr.rob.core.network.netty.event.NettyChannelReadEvent
import fr.rob.core.network.netty.plugin.security.rule.RuleInterface

class CheckOperatorRule(private val opcodesToProtect: Array<Int>) : RuleInterface {

    override fun support(context: Any): Boolean {
        return context is NettyChannelReadEvent
                && opcodesToProtect.contains(context.opcode)
    }

    override fun apply(context: Any): Boolean {
        context as NettyChannelReadEvent

        val res = context.session.isLocal() && context.session.isAuthenticated

        if (!res) {
            context.shouldProcessPacket = false
            context.stopPropagation()
        }

        return res
    }
}
