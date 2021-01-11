package fr.rob.game.infrastructure.database.event

import fr.rob.game.domain.event.Event

abstract class DatabaseEvent(val sql: String, val time: Long) : Event() {
}
