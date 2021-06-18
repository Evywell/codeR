package fr.rob.cli

import fr.rob.cli.command.auth.dev.AuthCommandHandler
import fr.rob.cli.command.dummy.DummyCommandHandler
import fr.rob.cli.command.strategy.StrategyCommandHandler
import fr.rob.cli.security.auth.AuthenticationProcess
import fr.rob.cli.security.strategy.StrategyProcess
import fr.rob.core.ENV_DEV

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val console = ConsoleApplication(InputHandler(), OutputHandler())

            val authProcess = console.processManager.getOrMakeProcess(AuthenticationProcess::class)
            val strategyProcess = console.processManager.getOrMakeProcess(StrategyProcess::class)

            console.registerCommand(DummyCommandHandler())
            console.registerCommand(AuthCommandHandler(ENV_DEV, authProcess))
            console.registerCommand(StrategyCommandHandler(strategyProcess))
            console.run()

            // Thread with the input / command handler => calling output
            // Thread receiving responses calling output
        }
    }
}
