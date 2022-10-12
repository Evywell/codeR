package fr.rob.orchestrator.api.grpc

import fr.rob.orchestrator.shared.entities.grpc.InstanceCreationResponse
import fr.rob.orchestrator.shared.entities.grpc.InstanceGrpc
import fr.rob.orchestrator.shared.entities.grpc.InstanceInfo
import io.grpc.stub.StreamObserver

class InstanceServiceImpl : InstanceGrpc.InstanceImplBase() {
    override fun create(request: InstanceInfo, responseObserver: StreamObserver<InstanceCreationResponse>) {

    }
}
