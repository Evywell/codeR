package fr.rob.gateway.extension.game

import fr.rob.core.network.v2.ClientInterface
import fr.rob.gateway.message.extension.game.GameProto.Packet as GamePacket

class GameNode(private val client: ClientInterface<GamePacket>) {

    fun send(packet: GamePacket) {
        client.send(packet)
    }
}
