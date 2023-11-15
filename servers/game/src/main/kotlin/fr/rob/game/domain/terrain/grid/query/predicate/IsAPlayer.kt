package fr.rob.game.domain.terrain.grid.query.predicate

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.player.Player
import fr.rob.game.domain.terrain.grid.query.GridQueryPredicateInterface

class IsAPlayer : GridQueryPredicateInterface {
    override fun resolve(subject: WorldObject): Boolean = subject is Player && subject.guid.isPlayer()
}