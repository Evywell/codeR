package fr.rob.core.network.strategy

interface ServerStrategyInterface {

    fun authorizeNewConnection(): Boolean
}
