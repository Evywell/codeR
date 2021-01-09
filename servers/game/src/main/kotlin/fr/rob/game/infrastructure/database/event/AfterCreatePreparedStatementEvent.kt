package fr.rob.game.infrastructure.database.event

class AfterCreatePreparedStatementEvent(sql: String, time: Long) : DatabaseEvent(sql, time) {

    override fun getName(): String = AFTER_CREATE_PREPARED_STATEMENT

    companion object {
        const val AFTER_CREATE_PREPARED_STATEMENT = "AfterCreatePreparedStatementEvent"
    }

}
