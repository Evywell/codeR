package fr.rob.cli.command.strategy

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import fr.rob.cli.security.strategy.StrategyProcess

class StrategyCommand(private val strategyProcess: StrategyProcess) : CliktCommand() {
    private val name by option().choice(STRATEGY_DISCARD)

    override fun run() {
        if (name == null) {
            return
        }

        strategyProcess.useStrategy(name!!)
    }

    companion object {
        const val STRATEGY_DISCARD = "discard"
    }
}
