package fr.rob.game.domain.terrain.grid.query

import fr.rob.game.domain.entity.WorldObject

interface GridQueryPredicateInterface {
    fun resolve(subject: WorldObject): Boolean
}
