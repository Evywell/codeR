package fr.rob.game.map.grid.query

import fr.rob.game.entity.WorldObject

interface GridQueryPredicateInterface {
    fun resolve(subject: WorldObject): Boolean
}
