package fr.rob.gateway.extension.realm

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.extension.realm.opcode.CMSG_REALM_JOIN_WORLD
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class RealmPacketDispatcher(
    realmClient: RealmClient,
    realmService: RealmService,
    private val gameNodes: GameNodes
) : PacketDispatcherInterface {

    private val tmpJoinWorldOpcodeHandler = TmpJoinWorldOpcodeHandler(realmClient, realmService)

    override fun support(packet: Packet, session: GatewaySession): Boolean =
        session.isAuthenticated && Packet.Context.REALM == packet.context

    override fun dispatch(packet: Packet, session: GatewaySession) {
        when (packet.opcode) {
            CMSG_REALM_JOIN_WORLD -> {
                tmpJoinWorldOpcodeHandler.run(packet, session, gameNodes)

                return
            }
        }
    }

    override fun transmitInterruption(session: GatewaySession) {}
}
