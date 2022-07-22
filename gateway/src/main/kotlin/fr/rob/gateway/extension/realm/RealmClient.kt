package fr.rob.gateway.extension.realm

import fr.raven.proto.message.realm.RealmProto
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.realm.gamenode.GameNodes

class RealmClient(
    private val gameNodes: GameNodes,
    private val realmService: RealmService
) : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onPacketReceived(packet: Packet) {
        when (packet.readOpcode()) {
            // @todo for the moment I chose a random opcode
            1 -> {
                val bindCharacterPacket = RealmProto.BindCharacterToNode.parseFrom(packet.toByteArray())
                realmService.bindCharacterToNode(bindCharacterPacket, gameNodes, "join-world")

                return
            }
        }
    }
}
