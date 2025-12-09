package fr.rob.world.service.infra

import fr.rob.core.database.ConnectionManager
import fr.rob.world.service.infra.config.Config
import fr.rob.world.service.infra.dependency.modules
import fr.rob.world.service.packages.characters.app.api.DescribeCharacter
import fr.rob.world.service.packages.characters.infra.grpc.service.CharacterServiceImpl
import io.grpc.ServerBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class App(private val config: Config) : KoinComponent {
    private val connectionManager: ConnectionManager by inject()
    private val describeCharacter: DescribeCharacter by inject()

    fun run() {
        startKoin {
            modules(*modules)
        }

        val rpcServer = ServerBuilder
            .forPort(config.grpcServerPort)
            .addService(CharacterServiceImpl(describeCharacter))
            .build()

        rpcServer.start()

        rpcServer.awaitTermination()
    }
}
