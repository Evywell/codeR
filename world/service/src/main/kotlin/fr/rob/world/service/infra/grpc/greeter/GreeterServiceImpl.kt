package fr.rob.world.service.infra.grpc.greeter

import fr.rob.world.api.grpc.GreeterGrpc
import fr.rob.world.api.grpc.HelloReply
import fr.rob.world.api.grpc.HelloRequest
import io.grpc.stub.StreamObserver

class GreeterServiceImpl : GreeterGrpc.GreeterImplBase() {
    override fun sayHello(request: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        val greeting = StringBuilder("Hello, ")
            .append(request.name)
            .toString()

        val response = HelloReply.newBuilder()
            .setMessage(greeting)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
