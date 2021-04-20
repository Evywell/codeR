package fr.rob.login.test.feature.service.network

import fr.rob.core.network.Packet

abstract class ListenerCallback(private val opcode: Int, private val packet: Packet, private val msg: Any?) {

    abstract fun call()
}
