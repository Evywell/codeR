package sandbox.scenario.benchmark

import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.message.extension.game.GameProto

class BenchmarkServer(private val onNewConnection: OnNewConnectionInterface? = null) : Server<GameProto.Packet>() {
    override fun onPacketReceived(session: Session, packet: GameProto.Packet) {
        TODO("Not yet implemented")
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onNewConnection(id: String, session: Session) {
        super.onNewConnection(id, session)

        onNewConnection?.call(id, session)
    }
}
