package fr.rob.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.int
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

class Hello : CliktCommand() {
    val count: Int by option(help = "Number of greetings").int().default(1)
    val name: String by option(help = "The person to greet").prompt("Your name")

    override fun run() {
        repeat(count) {
            echo("Hello $name!")
        }
    }
}
