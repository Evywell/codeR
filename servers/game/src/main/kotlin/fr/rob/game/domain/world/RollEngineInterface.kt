package fr.rob.game.domain.world

interface RollEngineInterface {
    fun roll(min: Int, max: Int): Int
}
