package fr.rob.game.domain.spell.target

import fr.rob.game.domain.entity.WorldObject
import java.util.Optional

interface SpellTargetInterface {
    fun getTargets(): Array<WorldObject>
    fun getFirstTarget(): Optional<WorldObject>
}
