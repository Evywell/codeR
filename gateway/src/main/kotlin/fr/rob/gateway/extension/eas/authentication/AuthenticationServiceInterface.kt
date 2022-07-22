package fr.rob.gateway.extension.eas.authentication

import fr.raven.proto.message.eas.EasProto
import fr.rob.gateway.network.GatewaySession

interface AuthenticationServiceInterface {
    fun support(packet: EasProto.EasPacket): Boolean

    /**
     * @throws fr.rob.gateway.extension.eas.authentication.exception.AuthenticationException
     */
    fun authenticate(session: GatewaySession, packet: EasProto.EasPacket)
}
