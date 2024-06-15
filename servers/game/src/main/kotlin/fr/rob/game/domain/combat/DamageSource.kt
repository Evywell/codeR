package fr.rob.game.domain.combat

import fr.rob.game.domain.entity.WorldObject

data class DamageSource(val source: WorldObject, val amount: Int)
