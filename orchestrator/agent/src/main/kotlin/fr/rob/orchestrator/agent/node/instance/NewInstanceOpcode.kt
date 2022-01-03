package fr.rob.orchestrator.agent.node.instance

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface
import fr.rob.orchestrator.shared.entities.CreateInstanceRequestProto
import fr.rob.orchestrator.shared.opcode.API_INSTANCE_CREATED

class NewInstanceOpcode(private val adapter: NodeAgentAdapterInterface) : ProtobufOpcodeFunction() {
    override fun getMessageType(): Message = CreateInstanceRequestProto.CreateMapInstanceRequest.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        message as CreateInstanceRequestProto.CreateMapInstanceRequest

        val zoneId = if (message.zoneId == 0) null else message.zoneId

        adapter.handleNewInstanceRequest(message.node, message.mapId, zoneId)

        val instanceCreateMessage = CreateInstanceRequestProto.ConfirmMapInstanceCreated.newBuilder()
            .setRequestId(message.requestId)
            .setMapId(message.mapId)
            .setZoneId(message.zoneId)
            .build()

        session.send(Packet(API_INSTANCE_CREATED, instanceCreateMessage.toByteArray()))
    }
}
