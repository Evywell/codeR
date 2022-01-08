package fr.rob.orchestrator.api.network.grpc

import fr.rob.orchestrator.api.grpc.HelloRequest
import fr.rob.orchestrator.api.grpc.HelloResponse
import fr.rob.orchestrator.api.grpc.HelloServiceGrpc
import io.grpc.stub.StreamObserver

class HelloServiceImpl : HelloServiceGrpc.HelloServiceImplBase() {

    override fun hello(request: HelloRequest, responseObserver: StreamObserver<HelloResponse>) {
        val greeting = StringBuilder("Hello, ")
            .append(request.firstName)
            .append(" ")
            .append(request.lastName)
            .toString()

        val response = HelloResponse.newBuilder()
            .setGreeting(greeting)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
