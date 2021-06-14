package fr.rob.cli.command.strategy

import fr.rob.cli.command.CommandHandlerInterface
import fr.rob.cli.security.strategy.StrategyProcess

class StrategyCommandHandler(private val strategyProcess: StrategyProcess) : CommandHandlerInterface {

    override fun canHandle(command: String): Boolean = command.startsWith("strategy")

    override fun handle(arguments: List<String>) {
        StrategyCommand(strategyProcess).main(arguments)
    }
}
