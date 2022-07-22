package fr.rob.gateway.extension.game

import fr.rob.core.network.v2.ClientInterface
import fr.raven.proto.message.game.GameProto.Packet as GamePacket

class GameNode(val label: String, private val client: ClientInterface<GamePacket>) {

    fun send(packet: GamePacket) {
        client.send(packet)
    }
}
