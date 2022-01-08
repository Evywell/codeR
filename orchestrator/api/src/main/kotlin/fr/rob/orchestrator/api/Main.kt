package fr.rob.orchestrator.api

import fr.rob.core.database.Connection
import fr.rob.core.log.LoggerFactory
import fr.rob.core.network.v2.netty.NettyServer
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.api.composer.RequestComposer
import fr.rob.orchestrator.api.grpc.HelloRequest
import fr.rob.orchestrator.api.grpc.HelloServiceGrpc
import fr.rob.orchestrator.api.instance.DefaultInstancesRepository
import fr.rob.orchestrator.api.instance.DefaultInstancesRepositoryInterface
import fr.rob.orchestrator.api.instance.InstanceManager
import fr.rob.orchestrator.api.instance.InstancesRepository
import fr.rob.orchestrator.api.network.OrchestratorServer
import fr.rob.orchestrator.api.network.grpc.HelloServiceImpl
import fr.rob.orchestrator.api.node.NodeManager
import fr.rob.orchestrator.shared.Orchestrator
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.ServerBuilder
import io.grpc.stub.MetadataUtils
import io.grpc.stub.MetadataUtils.newAttachHeadersInterceptor

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello World")

            val processManager = ProcessManager()

            // Dependencies
            val dbPlayers = Connection("mysql_game", 3306, "testing", "passwordtesting", "players")
            val dbConfig = Connection("mysql_game", 3306, "testing", "passwordtesting", "config")

            processManager.registerProcess(NodeManager::class) {
                NodeManager()
            }

            processManager.registerProcess(DefaultInstancesRepositoryInterface::class) {
                DefaultInstancesRepository(dbConfig)
            }

            processManager.registerProcess(InstanceManager::class) {
                InstanceManager(InstancesRepository(dbPlayers))
            }

            processManager.registerProcess(RequestComposer::class) {
                RequestComposer()
            }

            val orchestrator = Orchestrator(1, "orchestrator:12345", "azert")
            val server = OrchestratorServer(orchestrator, LoggerFactory.create("server"), processManager)
            val serverProcess = NettyServer(12345, server, false)

            server.start(serverProcess)

            val rpcServer = ServerBuilder
                .forPort(12346)
                .addService(HelloServiceImpl())
                .intercept(AuthHeaderInterceptor())
                .build()

            rpcServer.start()

            val channel = ManagedChannelBuilder.forAddress("localhost", 12346)
                .usePlaintext()
                .build()

            val headers = Metadata()
            val key = Metadata.Key.of("Auth", Metadata.ASCII_STRING_MARSHALLER)
            headers.put(key, "Hello")

            val stub = HelloServiceGrpc.newBlockingStub(channel)
                .withInterceptors(newAttachHeadersInterceptor(headers))

            val helloResponse = stub.hello(HelloRequest.newBuilder()
                .setFirstName("Axel")
                .setLastName("LEDUC")
                .build()
            )

            println(helloResponse.greeting)

            channel.shutdown()
            rpcServer.shutdown()

            rpcServer.awaitTermination()
        }
    }
}
