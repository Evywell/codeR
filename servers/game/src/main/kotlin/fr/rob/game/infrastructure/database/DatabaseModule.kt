package fr.rob.game.infrastructure.database

import fr.rob.core.AbstractModule
import fr.rob.game.domain.event.EventInterface
import fr.rob.game.domain.event.EventListenerInterface
import fr.rob.game.domain.event.EventManagerInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.game.infrastructure.database.event.AfterCreatePreparedStatementEvent
import fr.rob.game.infrastructure.database.event.AfterSQLReqExecutedEvent
import fr.rob.game.infrastructure.database.event.DatabaseEvent
import fr.rob.core.log.LoggerFactory

class DatabaseModule(private val eventManager: EventManagerInterface) : AbstractModule() {

    override fun boot() {
        // Registering SQL Events
        val logger = LoggerFactory.create("SQL")

        val sqlLoggerEventListener = SQLLoggerEventListener(logger)

        eventManager.addEventListener(
            AfterCreatePreparedStatementEvent.AFTER_CREATE_PREPARED_STATEMENT,
            sqlLoggerEventListener
        )

        eventManager.addEventListener(
            AfterSQLReqExecutedEvent.AFTER_SQL_REQ_EXECUTED_EVENT,
            sqlLoggerEventListener
        )
    }
}

class SQLLoggerEventListener(private val logger: LoggerInterface) : EventListenerInterface {

    override fun process(event: EventInterface) {
        val databaseEvent = event as DatabaseEvent

        logger.debug("Execution time: ${databaseEvent.time} ms. Stmt: ${databaseEvent.sql}")
    }
}
