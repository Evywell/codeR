package fr.rob.core.database.runner

import fr.rob.core.infrastructure.database.PreparedStatement

open class StatementCommand(private val stmt: PreparedStatement, private val isForUpdate: Boolean = false) :
    CommandInterface {

    override fun execute(): Any {
        if (isForUpdate) {
            return stmt.executeUpdate()
        }

        return stmt.execute()
    }
}
