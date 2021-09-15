package fr.rob.core.database.runner

import fr.rob.core.infrastructure.database.PreparedStatement

class InsertStatementCommand(private val stmt: PreparedStatement, private val message: String) : CommandInterface {

    override fun execute(): Any {
        return stmt.executeInsertOrThrow(message)
    }
}
