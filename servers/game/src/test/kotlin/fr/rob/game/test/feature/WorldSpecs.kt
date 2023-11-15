package fr.rob.game.test.feature

import fr.rob.game.infra.dependency.databaseModule
import fr.rob.game.infra.dependency.globalModule
import fr.rob.game.infra.dependency.mapModule
import fr.rob.game.infra.dependency.opcodeModule
import fr.rob.game.infra.dependency.queueModule
import org.koin.core.context.startKoin
import org.koin.test.KoinTest

abstract class WorldSpecs : KoinTest {
    fun init() {
        startKoin {
            modules(globalModule, databaseModule, mapModule, queueModule, opcodeModule)
        }
    }
}
