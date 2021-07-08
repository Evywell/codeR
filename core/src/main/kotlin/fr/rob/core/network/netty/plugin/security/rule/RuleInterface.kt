package fr.rob.core.network.netty.plugin.security.rule

interface RuleInterface {

    fun support(context: Any): Boolean
    fun apply(context: Any): Boolean
}
