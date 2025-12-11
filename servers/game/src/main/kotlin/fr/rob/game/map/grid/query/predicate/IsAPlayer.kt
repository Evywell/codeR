package fr.rob.game.map.grid.query.predicate

import fr.rob.game.entity.WorldObject
import fr.rob.game.player.Player
import fr.rob.game.map.grid.query.GridQueryPredicateInterface

class IsAPlayer : GridQueryPredicateInterface {
    override fun resolve(subject: WorldObject): Boolean = subject is Player && subject.guid.isPlayer()
}
