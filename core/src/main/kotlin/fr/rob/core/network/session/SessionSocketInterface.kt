package fr.rob.core.network.session

import fr.rob.core.network.Packet

interface SessionSocketInterface {

    fun getIp(): String
    fun send(packet: Packet)
    fun close()
}
