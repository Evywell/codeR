package fr.rob.gateway.network

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.eas.EasPacketDispatcher
import fr.rob.gateway.extension.eas.EasService
import fr.rob.gateway.extension.eas.authentication.DevAuthenticationService
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.game.GameNodeClient
import fr.rob.gateway.extension.game.GameNodePacketBuilder
import fr.rob.gateway.extension.game.GameNodePacketDispatcher
import fr.rob.gateway.extension.game.network.netty.client.GameNodeNettyClient
import fr.rob.gateway.extension.realm.RealmClient
import fr.rob.gateway.extension.realm.RealmPacketDispatcher
import fr.rob.gateway.extension.realm.RealmService
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class Gateway : Server<Packet>() {

    private val dispatchers = ArrayList<PacketDispatcherInterface>()
    private val sessionIdentifiers = HashMap<Int, String>()

    init {
        // @todo TO REMOVE
        val client = GameNodeClient(this)
        val process = GameNodeNettyClient("127.0.0.1", 22222, client)
        val gameNode = GameNode("NODE_LABEL_TEST", client)
        val gameNodePacketDispatcher = GameNodePacketDispatcher(GameNodePacketBuilder())

        process.start()

        val nodesAggregate = GameNodes()
        // @todo Do not add any node at the beginning, we must wait a request from the client
        nodesAggregate.addNode(gameNode)

        val realmService = RealmService(this, gameNodePacketDispatcher)

        val realmClient = RealmClient(nodesAggregate, realmService)
        // val realmProcess = NettyClient("127.0.0.1", 55501, realmClient)
        // realmProcess.start()

        val easService = EasService(this, arrayOf(DevAuthenticationService()))

        dispatchers.add(EasPacketDispatcher(easService))
        dispatchers.add(RealmPacketDispatcher(realmClient))
        dispatchers.add(gameNodePacketDispatcher)
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
}
