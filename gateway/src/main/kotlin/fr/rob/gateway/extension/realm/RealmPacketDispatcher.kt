package fr.rob.gateway.extension.realm

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class RealmPacketDispatcher(
    realmClient: RealmClient,
) : PacketDispatcherInterface {

    private val tmpJoinWorldOpcodeHandler = TmpJoinWorldOpcodeHandler(realmClient)

    override fun support(packet: Packet, session: GatewaySession): Boolean =
        session.isAuthenticated && Packet.Context.REALM == packet.context

    override fun dispatch(packet: Packet, session: GatewaySession) {
        when (packet.opcode) {
            CMSG_REALM_JOIN_WORLD -> {
                tmpJoinWorldOpcodeHandler.run(packet, session)

                return
            }
        }
    }
}
