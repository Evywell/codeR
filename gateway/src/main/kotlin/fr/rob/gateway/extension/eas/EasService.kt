package fr.rob.gateway.extension.eas

import fr.raven.proto.message.eas.EasProto.EasAuthenticationResult
import fr.raven.proto.message.eas.EasProto.EasPacket
import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.gateway.extension.eas.authentication.AuthenticationServiceInterface
import fr.rob.gateway.extension.eas.authentication.exception.UnknownAuthenticationService
import fr.rob.gateway.network.Gateway
import fr.rob.gateway.network.GatewaySession

class EasService(private val gateway: Gateway, private val authServices: Array<AuthenticationServiceInterface>) {
    fun authenticate(session: GatewaySession, packet: EasPacket) {
        if (session.isAuthenticated) {
            // Already authenticated, skip...
            return
        }

        authenticateByDedicatedService(session, packet)

        val authenticationSucceeded = session.isAuthenticated

        if (authenticationSucceeded) {
            gateway.registerSessionIdentifier(session)
        }

        val authResultPacket = EasAuthenticationResult.newBuilder()
            .setIsAuthenticatedSuccessfully(authenticationSucceeded)
            .build()

        session.send(
            Packet.newBuilder()
                .setContext(Packet.Context.EAS)
                .setOpcode(EAS_AUTHENTICATION_RESULT)
                .setBody(authResultPacket.toByteString())
                .build()
        )
    }

    /**
     * @throws UnknownAuthenticationService
     */
    private fun authenticateByDedicatedService(session: GatewaySession, packet: EasPacket) {
        val authService = retrieveAuthenticateServiceFromPacket(packet)

        authService.authenticate(session, packet)
    }

    private fun retrieveAuthenticateServiceFromPacket(packet: EasPacket): AuthenticationServiceInterface {
        for (authService in authServices) {
            if (authService.support(packet)) {
                return authService
            }
        }

        throw UnknownAuthenticationService()
    }
}
