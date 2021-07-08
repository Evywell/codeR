package fr.rob.core.network.netty.event

open class NettyEvent {

    var isStopped = false

    fun stopPropagation() {
        isStopped = true
    }
}
