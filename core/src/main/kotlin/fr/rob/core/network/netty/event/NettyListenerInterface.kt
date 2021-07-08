package fr.rob.core.network.netty.event

interface NettyListenerInterface {

    fun invoke(event: NettyEvent)
}
