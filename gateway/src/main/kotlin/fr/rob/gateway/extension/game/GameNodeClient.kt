package fr.rob.gateway.extension.game

import fr.raven.proto.message.game.GameProto.Packet
import fr.raven.proto.message.game.setup.InitializeOpcodeProto.InitializationSucceed
import fr.raven.proto.message.gateway.GatewayProto
import fr.raven.proto.message.realm.RealmProto.JoinTheWorld
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.network.Gateway

class GameNodeClient(private val gateway: Gateway) : AbstractClient<Packet>() {
    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: Packet) {
        println("Packet received with opcode ${packet.opcode}")
        val accountId = packet.sender
        val session = gateway.findSessionByAccountId(accountId)

        when (packet.opcode) {
            0x99 -> {
                val message = InitializationSucceed.parseFrom(packet.body)
                val characterInQueue = session.characterInQueue

                if (message.actionToInitiate == "join-world" && characterInQueue != null) {
                    proceedJoinWorld(accountId, characterInQueue)

                    return
                }

                println("No action to initiate or unknown one")
            }
        }

        session.send(
            GatewayProto.Packet.newBuilder()
                .setContext(GatewayProto.Packet.Context.GAME)
                .setOpcode(packet.opcode)
                .setBody(packet.body)
                .build()
        )
    }

    private fun proceedJoinWorld(accountId: Int, characterInQueue: Int) {
        val joinWorldPacket = JoinTheWorld.newBuilder()
            .setCharacterId(characterInQueue)
            .build()

        val gamePacket = Packet.newBuilder()
            .setOpcode(0x01)
            .setSender(accountId)
            .setBody(joinWorldPacket.toByteString())
            .build()

        send(gamePacket)
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
