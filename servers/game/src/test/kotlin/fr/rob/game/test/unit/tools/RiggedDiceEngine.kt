package fr.rob.game.test.unit.tools

import fr.rob.game.domain.world.RollEngineInterface

class RiggedDiceEngine(var nextRollResult: Int? = null) : RollEngineInterface {

    override fun roll(min: Int, max: Int): Int {
        if (nextRollResult != null && nextRollResult!! >= min && nextRollResult!! <= max) {
            return nextRollResult!!
        }

        return max
    }
}
