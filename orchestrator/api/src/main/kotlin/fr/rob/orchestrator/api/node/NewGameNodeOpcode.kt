package fr.rob.orchestrator.api.node

import com.google.protobuf.Message
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.orchestrator.api.composer.RequestComposer
import fr.rob.orchestrator.api.instance.DefaultInstance
import fr.rob.orchestrator.api.instance.DefaultInstancesRepositoryInterface
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.api.network.OrchestratorSession
import fr.rob.orchestrator.shared.opcode.AGENT_REQUEST_CREATE_MAP_INSTANCE
import fr.rob.orchestrator.shared.entities.NewGameNodeProto.NewGameNode as NewGameNodeMessage

class NewGameNodeOpcode(
    private val nodeManager: NodeManager,
    private val defaultInstancesRepository: DefaultInstancesRepositoryInterface,
    private val instanceManager: InstanceManager,
    private val requestComposer: RequestComposer
) : ProtobufOpcodeFunction() {
    override fun getMessageType(): Message = NewGameNodeMessage.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        session as OrchestratorSession
        message as NewGameNodeMessage

        val node = nodeManager.registerNode(message.name, message.port)
        val defaultInstances = defaultInstancesRepository.getDefaultInstancesByNode(message.name)

        val instancesToSend =
            instanceManager.createMultipleForNode(node, DefaultInstance.collectionIntoInstanceInfo(defaultInstances))

        for (instance in instancesToSend) {
            val request = requestComposer.create(instance, node)

            session.send(Packet(AGENT_REQUEST_CREATE_MAP_INSTANCE, request.buildProto().toByteArray()))
        }
    }
}
