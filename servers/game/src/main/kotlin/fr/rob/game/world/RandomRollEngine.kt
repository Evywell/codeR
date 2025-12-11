package fr.rob.game.world

class RandomRollEngine : RollEngineInterface {
    override fun roll(min: Int, max: Int): Int = (min..max).random()
}
