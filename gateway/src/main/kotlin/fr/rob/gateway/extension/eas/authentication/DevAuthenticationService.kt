package fr.rob.gateway.extension.eas.authentication

import fr.raven.proto.message.eas.EasProto.EasPacket
import fr.rob.gateway.network.GatewaySession

class DevAuthenticationService : AuthenticationServiceInterface {
    override fun support(packet: EasPacket): Boolean =
        EasPacket.Type.DEV == packet.authType

    override fun authenticate(session: GatewaySession, packet: EasPacket) {
        session.authenticate(packet.devAuthPacket.userId)
    }
}
