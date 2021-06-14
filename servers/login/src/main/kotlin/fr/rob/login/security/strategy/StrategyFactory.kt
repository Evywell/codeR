package fr.rob.login.security.strategy

import fr.rob.core.network.strategy.NullServerStrategy
import fr.rob.core.network.strategy.ServerStrategyInterface
import kotlin.RuntimeException

class StrategyFactory {

    companion object {

        private const val STRATEGY_OPEN = "open"
        private const val STRATEGY_DISCARD = "discard"

        fun createStrategy(name: String): ServerStrategyInterface {
            when (name) {
                STRATEGY_OPEN -> return NullServerStrategy()
                STRATEGY_DISCARD -> return DiscardStrategy()
            }

            throw RuntimeException("There is no strategy with name $name")
        }
    }
}
