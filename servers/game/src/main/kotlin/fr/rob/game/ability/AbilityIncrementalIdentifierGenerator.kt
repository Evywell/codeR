package fr.rob.game.ability

class AbilityIncrementalIdentifierGenerator {
    private var internalCounter: Int = 0

    fun getNextIdentifier(): Int {
        internalCounter += 1
        return internalCounter
    }
}