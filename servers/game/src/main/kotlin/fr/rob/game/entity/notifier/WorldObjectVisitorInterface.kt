package fr.rob.game.entity.notifier

import fr.rob.game.entity.WorldObject

interface WorldObjectVisitorInterface {
    fun visit(gameObject: WorldObject)
}
