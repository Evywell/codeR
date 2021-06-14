package fr.rob.core.network.strategy

class NullServerStrategy : ServerStrategyInterface {

    override fun authorizeNewConnection(): Boolean = true
}
