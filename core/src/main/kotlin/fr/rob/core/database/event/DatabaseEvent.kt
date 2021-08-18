package fr.rob.core.database.event

import fr.rob.core.event.Event

abstract class DatabaseEvent(val sql: String, val time: Long) : Event()
