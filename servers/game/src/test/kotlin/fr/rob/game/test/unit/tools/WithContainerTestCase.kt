package fr.rob.game.test.unit.tools

import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class WithContainerTestCase : KoinTest {
    @BeforeAll
    fun setUp() {
        val unitTestModule = module {
            single { ObjectGuidGenerator() }
            single { DummyPlayerBuilder(get()) }
        }

        startKoin {
            modules(unitTestModule)
        }
    }

    @AfterAll
    fun tearDown() {
        stopKoin()
    }
}
