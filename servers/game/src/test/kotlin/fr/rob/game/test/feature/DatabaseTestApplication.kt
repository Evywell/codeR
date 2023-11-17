package fr.rob.game.test.feature

import fr.rob.core.database.Connection
import fr.rob.game.DB_WORLD
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class DatabaseTestApplication : TestApplication() {
    protected lateinit var databaseConnection: Connection

    @BeforeAll
    override fun launchApp() {
        super.launchApp()
        // Start transaction
        databaseConnection = connectionPoolManager.getPool(DB_WORLD)!!.getNextConnection()
        databaseConnection.executeStatement("START TRANSACTION;")
    }

    @AfterAll
    override fun shutdownApp() {
        super.shutdownApp()

        // rollback transaction
        databaseConnection.executeStatement("ROLLBACK;")
    }
}
