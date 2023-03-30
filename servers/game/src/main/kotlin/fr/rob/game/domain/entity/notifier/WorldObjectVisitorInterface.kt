package fr.rob.game.domain.entity.notifier

import fr.rob.game.domain.entity.WorldObject

interface WorldObjectVisitorInterface {
    fun visit(gameObject: WorldObject)
}
