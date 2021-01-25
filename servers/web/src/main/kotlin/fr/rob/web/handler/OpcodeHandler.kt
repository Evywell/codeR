package fr.rob.web.handler

import com.google.protobuf.util.JsonFormat
import fr.rob.client.network.Client
import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.opcode.ProtobufOpcodeFunction
import fr.rob.web.network.Request
import fr.rob.web.network.RouteHandler
import fr.rob.game.domain.opcode.OpcodeHandler as GameOpcodeHandler

class OpcodeHandler(private val client: Client, private val opcodeHandler: GameOpcodeHandler) : RouteHandler() {

    override fun handle(request: Request): String {
        val opcode = request.routeParameters?.get("id") as Int

        val opcodeFunction = opcodeHandler.getOpcodeFunction(opcode) as ProtobufOpcodeFunction

        val builder = opcodeFunction.getMessageType().toBuilder()
        JsonFormat.parser().ignoringUnknownFields().merge(request.body, builder)

        val packet = Packet(opcode, builder.build().toByteArray())

        client.send(packet)

        return "Vous avez demandé l'opcode $opcode"
    }
}
