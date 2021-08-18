package fr.rob.core.database.event

class AfterSQLReqExecutedEvent(sql: String, time: Long) : DatabaseEvent(sql, time) {

    override fun getName(): String = AFTER_SQL_REQ_EXECUTED_EVENT

    companion object {
        const val AFTER_SQL_REQ_EXECUTED_EVENT = "AfterSQLReqExecutedEvent"
    }
}
