package fr.rob.orchestrator.nodes

import com.google.protobuf.Message
import fr.rob.core.network.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.orchestrator.NewGameNodeProto
import fr.rob.orchestrator.instances.InstanceManager
import fr.rob.orchestrator.instances.request.RequestNewInstanceProcess
import fr.rob.orchestrator.network.OrchestratorSession

class NewGameNodeOpcode(
    private val nodeManager: GameNodeManager,
    private val instanceManager: InstanceManager,
    private val requestNewInstanceProcess: RequestNewInstanceProcess
) :
    ProtobufOpcodeFunction() {
    override fun getMessageType(): Message = NewGameNodeProto.NewGameNode.getDefaultInstance()

    override fun call(session: Session, message: Any) {
        session as OrchestratorSession
        message as NewGameNodeProto.NewGameNode

        // Register the new node in the manager
        val node = nodeManager.register(session.getIp(), message, session)

        // Create mandatory instances for this node
        val instances = instanceManager.createInstancesForNode(node)

        for (instance in instances) {
            val request = requestNewInstanceProcess.createRequest(instance)

            session.requestNewInstance(request)
        }
    }
}
