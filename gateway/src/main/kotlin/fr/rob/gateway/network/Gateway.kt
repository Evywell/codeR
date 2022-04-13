package fr.rob.gateway.network

import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.game.GameNode
import fr.rob.gateway.extension.game.GameNodePacketBuilder
import fr.rob.gateway.extension.game.GameNodePacketDispatcher
import fr.rob.gateway.extension.game.network.GameNodeClient
import fr.rob.gateway.extension.game.network.netty.GameNodeNettyClient
import fr.rob.gateway.message.GatewayProto.Packet
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface

class Gateway : Server<Packet>() {

    private val dispatchers = ArrayList<PacketDispatcherInterface>()

    init {
        // @todo TO REMOVE
        val client = GameNodeClient()
        val process = GameNodeNettyClient("127.0.0.1", 22222, client)
        val gameNode = GameNode(client)

        process.start()

        dispatchers.add(GameNodePacketDispatcher(GameNodePacketBuilder(), gameNode))
    }

    override fun onPacketReceived(session: Session, packet: Packet) {
        session as GatewaySession
        // Retrieving session passport
        // Forward to the correct dispatcher
        for (dispatcher in dispatchers) {
            if (dispatcher.support(packet)) {
                dispatcher.dispatch(packet, session)
            }
        }
    }

    override fun createSession(socket: SessionSocketInterface): Session = GatewaySession(socket)
}
