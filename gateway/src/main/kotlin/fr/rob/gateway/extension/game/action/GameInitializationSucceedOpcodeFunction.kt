package fr.rob.gateway.extension.game.action

import com.google.protobuf.Message
import fr.raven.proto.message.game.GameProto
import fr.raven.proto.message.game.setup.InitializeOpcodeProto
import fr.raven.proto.message.realm.RealmProto
import fr.rob.gateway.extension.game.GameNodeClient
import fr.rob.gateway.extension.game.opcode.GameNodeFunctionParameters
import fr.rob.gateway.extension.game.opcode.GameNodeOpcodeFunction

class GameInitializationSucceedOpcodeFunction(private val gameNodeClient: GameNodeClient) : GameNodeOpcodeFunction() {
    override fun createMessageFromPacket(packet: GameProto.Packet): Message =
        InitializeOpcodeProto.InitializationSucceed.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: GameNodeFunctionParameters) {
        message as InitializeOpcodeProto.InitializationSucceed
        val packet = functionParameters.packet
        val characterInQueue = functionParameters.gatewaySession.characterInQueue

        if (message.actionToInitiate == "join-world" && characterInQueue != null) {
            proceedJoinWorld(packet.sender, characterInQueue)

            return
        }

        println("No action to initiate or unknown one")
    }

    private fun proceedJoinWorld(accountId: Int, characterInQueue: Int) {
        val joinWorldPacket = RealmProto.JoinTheWorld.newBuilder()
            .setCharacterId(characterInQueue)
            .build()

        val gamePacket = GameProto.Packet.newBuilder()
            .setOpcode(0x01)
            .setSender(accountId)
            .setBody(joinWorldPacket.toByteString())
            .build()

        gameNodeClient.send(gamePacket)
    }

    override fun isCallAuthorized(functionParameters: GameNodeFunctionParameters): Boolean = true
}
