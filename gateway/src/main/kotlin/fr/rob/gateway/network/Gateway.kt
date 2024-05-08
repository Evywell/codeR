package fr.rob.gateway.network

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.ExtensionInterface
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class Gateway : Server<Packet>() {

    private val dispatchers = ArrayList<PacketDispatcherInterface>()
    private val sessionIdentifiers = HashMap<Int, String>()

    val gameNodes = GameNodes()

    fun registerExtension(extension: ExtensionInterface): Gateway {
        dispatchers.add(extension.createDispatcher(this))

        return this
    }

    fun closeGameNodeConnection(gameNode: GameNode) {
        findSessionsByGameNode(gameNode).forEach { session -> session.kick() }

        gameNodes.removeGameNode(gameNode)
    }

    override fun onPacketReceived(session: Session, packet: Packet) {
        session as GatewaySession
        // Retrieving session passport
        // Forward to the correct dispatcher
        for (dispatcher in dispatchers) {
            if (dispatcher.support(packet, session)) {
                dispatcher.dispatch(packet, session)
            }
        }
    }

    override fun onNewConnection(id: String, session: Session) {
        super.onNewConnection(id, session)

        session as GatewaySession
        session.id = id
    }

    override fun onConnectionClosed(session: Session) {
        super.onConnectionClosed(session)
        session as GatewaySession

        for (dispatcher in dispatchers) {
            dispatcher.transmitInterruption(session)
        }
    }

    override fun createSession(socket: SessionSocketInterface): Session = GatewaySession(socket)

    fun findSessionByAccountId(accountId: Int): GatewaySession {
        return sessionFromIdentifier(
            sessionIdentifiers[accountId]
                ?: throw Exception("No session for account $accountId")
        ) as GatewaySession
    }

    fun registerSessionIdentifier(gatewaySession: GatewaySession) {
        sessionIdentifiers[gatewaySession.accountId!!] = gatewaySession.id
    }

    private fun findSessionsByGameNode(gameNode: GameNode): List<GatewaySession> {
        val matchingSession = ArrayList<GatewaySession>()

        sessionIdentifiers.forEach { (_, sessionId) ->
            val session = sessionFromIdentifier(sessionId) as GatewaySession

            if (session.currentGameNode == gameNode) {
                matchingSession.add(session)
            }
        }

        return matchingSession
    }
}
