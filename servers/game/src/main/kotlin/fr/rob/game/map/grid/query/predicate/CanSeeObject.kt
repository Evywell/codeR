package fr.rob.game.map.grid.query.predicate

import fr.rob.game.entity.WorldObject
import fr.rob.game.map.grid.query.GridQueryPredicateInterface

class CanSeeObject(private val source: WorldObject) : GridQueryPredicateInterface {
    override fun resolve(subject: WorldObject): Boolean {
        val position = subject.position
        val radius = 50
        val maxPosX = position.x + radius
        val minPosX = position.x - radius
        val maxPosY = position.y + radius
        val minPosY = position.y - radius

        return source.position.x in minPosX..maxPosX &&
            source.position.y in minPosY..maxPosY
    }
}
