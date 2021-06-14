package fr.rob.cli.command.auth.dev

import fr.rob.cli.command.CommandHandlerInterface
import fr.rob.cli.security.auth.AuthenticationProcess
import fr.rob.core.ENV_DEV

class AuthCommandHandler(private val env: String, private val authProcess: AuthenticationProcess) :
    CommandHandlerInterface {

    override fun canHandle(command: String): Boolean = command.startsWith("auth")

    override fun handle(arguments: List<String>) {
        if (env == ENV_DEV) {
            DevAuthCommand(authProcess).main(arguments)
        }
    }
}
