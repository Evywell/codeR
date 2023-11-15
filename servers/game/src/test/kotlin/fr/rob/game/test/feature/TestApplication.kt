package fr.rob.game.test.feature

import fr.rob.core.config.database.DatabaseConfig
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.DB_REALM
import fr.rob.game.DB_WORLD
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.infra.dependency.databaseModule
import fr.rob.game.infra.dependency.globalModule
import fr.rob.game.infra.dependency.mapModule
import fr.rob.game.infra.dependency.opcodeModule
import fr.rob.game.infra.dependency.queueModule
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get
import org.opentest4j.AssertionFailedError

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestApplication : KoinTest {
    private val objectGuidGenerator = ObjectGuidGenerator()

    @BeforeAll
    fun launchApp() {
        startKoin {
            modules(globalModule, databaseModule, mapModule, queueModule, opcodeModule)
        }

        createDatabasePools()
    }

    @AfterAll
    fun shutdownApp() {
        stopKoin()
    }

    protected fun assertContainsMessage(messages: List<StoreMessageSender.MessageSent>, expected: (StoreMessageSender.MessageSent) -> Boolean) {
        try {
            messages.first { container -> expected(container) }
        } catch (_: NoSuchElementException) {
            throw AssertionFailedError("No such message found in list", expected, messages)
        }
    }

    protected fun getGuidFromLow(low: ObjectGuid.LowGuid, type: ObjectGuid.GUID_TYPE): ObjectGuid =
        objectGuidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(low, type))

    private fun createDatabasePools() {
        val connectionPoolManager = get<ConnectionPoolManager> { parametersOf(6) }

        connectionPoolManager.createPool(
            DB_WORLD,
            DatabaseConfig(
                System.getProperty("mysql_unit.host"),
                System.getProperty("mysql_unit.tcp.3306").toLong(),
                "dev",
                "secret",
                "coder",
            ),
        )

        connectionPoolManager.createPool(
            DB_REALM,
            DatabaseConfig(
                System.getProperty("mysql_unit.host"),
                System.getProperty("mysql_unit.tcp.3306").toLong(),
                "dev",
                "secret",
                "coder",
            ),
        )
    }
}
