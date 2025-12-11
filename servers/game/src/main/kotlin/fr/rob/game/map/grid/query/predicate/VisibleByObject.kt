package fr.rob.game.map.grid.query.predicate

import fr.rob.game.entity.WorldObject
import fr.rob.game.map.grid.query.GridQueryPredicateInterface

class VisibleByObject(private val source: WorldObject) : GridQueryPredicateInterface {
    override fun resolve(subject: WorldObject): Boolean {
        val position = source.position
        val radius = 50
        val maxPosX = position.x + radius
        val minPosX = position.x - radius
        val maxPosY = position.y + radius
        val minPosY = position.y - radius

        return subject.position.x in minPosX..maxPosX &&
            subject.position.y in minPosY..maxPosY
    }
}
