package fr.rob.game.infrastructure.database.event


class AfterSQLReqExecutedEvent (private val sql: String, private val time: Long) : DatabaseEvent(sql, time) {

    override fun getName(): String = AFTER_SQL_REQ_EXECUTED_EVENT

    companion object {
        const val AFTER_SQL_REQ_EXECUTED_EVENT = "AfterSQLReqExecutedEvent"
    }
}
