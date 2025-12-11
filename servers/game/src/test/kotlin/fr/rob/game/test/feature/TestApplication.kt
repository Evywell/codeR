package fr.rob.game.test.feature

import fr.rob.core.config.database.DatabaseConfig
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.game.config.DB_REALM
import fr.rob.game.config.DB_WORLD
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.movement.spline.SplineMovementBrainInterface
import fr.rob.game.entity.movement.spline.StraightSplineMovementBrain
import fr.rob.game.config.databaseModule
import fr.rob.game.config.globalModule
import fr.rob.game.config.mapModule
import fr.rob.game.config.opcodeModule
import fr.rob.game.test.unit.sandbox.network.session.StoreMessageSender
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.opentest4j.AssertionFailedError

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestApplication : KoinTest {
    private val objectGuidGenerator = ObjectGuidGenerator()
    protected lateinit var connectionPoolManager: ConnectionPoolManager

    @BeforeAll
    open fun launchApp() {
        val testModule = module {
            single<SplineMovementBrainInterface> { StraightSplineMovementBrain() }
        }

        startKoin {
            modules(globalModule, databaseModule, mapModule, opcodeModule, testModule)
        }

        createDatabasePools()
    }

    @AfterAll
    open fun shutdownApp() {
        stopKoin()
    }

    protected fun assertContainsMessage(messages: List<StoreMessageSender.MessageSent>, expected: (StoreMessageSender.MessageSent) -> Boolean) {
        try {
            messages.first { container -> expected(container) }
        } catch (_: NoSuchElementException) {
            throw AssertionFailedError("No such message found in list", expected, messages)
        }
    }

    protected fun assertContainsMessage(messages: List<StoreMessageSender.MessageSent>, expectations: Array<(StoreMessageSender.MessageSent) -> Boolean>) {
        var depth = 0

        for (message in messages) {
            expectation@ for ((expectationIndex, expected) in expectations.withIndex()) {
                if (!expected(message)) {
                    break@expectation
                }

                if (depth < expectationIndex) {
                    depth = expectationIndex
                }
            }
        }

        if (depth < expectations.size - 1) {
            depth++

            throw AssertionFailedError("Assertion {$depth} failed")
        }
    }

    protected fun getGuidFromLow(low: ObjectGuid.LowGuid, type: ObjectGuid.GUID_TYPE): ObjectGuid =
        objectGuidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(low, type))

    private fun createDatabasePools() {
        connectionPoolManager = get<ConnectionPoolManager> { parametersOf(1) }

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
