package fr.rob.core.network.netty

import fr.rob.core.network.netty.plugin.security.rule.RuleInterface
import java.util.ArrayList

abstract class NettyPlugin {

    protected val rules = ArrayList<RuleInterface>()

    abstract fun boot(nettyServer: NettyServer)

    fun applyRules(context: Any) {
        for (rule in rules) {
            if (!rule.support(context)) {
                continue
            }

            if (!rule.apply(context)) {
                // We don't need to check the other rules
                return
            }
        }
    }

    fun rule(rule: RuleInterface): NettyPlugin {
        rules.add(rule)

        return this
    }
}
