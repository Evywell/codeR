package fr.rob.gateway.extension.realm.action

import com.google.protobuf.Message
import fr.raven.proto.message.realm.RealmProto
import fr.rob.core.network.Packet
import fr.rob.gateway.extension.realm.RealmService
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.extension.realm.opcode.RealmFunctionParameters
import fr.rob.gateway.extension.realm.opcode.RealmOpcodeFunction

class BindCharacterToNodeOpcodeFunction(
    private val gameNodes: GameNodes,
    private val realmService: RealmService
) : RealmOpcodeFunction() {
    override fun createMessageFromPacket(packet: Packet): Message =
        RealmProto.BindCharacterToNode.parseFrom(packet.toByteArray())

    override fun callForMessage(message: Message, functionParameters: RealmFunctionParameters) {
        message as RealmProto.BindCharacterToNode

        realmService.bindCharacterToNode(message, gameNodes)
    }

    override fun isCallAuthorized(functionParameters: RealmFunctionParameters): Boolean = true
}
