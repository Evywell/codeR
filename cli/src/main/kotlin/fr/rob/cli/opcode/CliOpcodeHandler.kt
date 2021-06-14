package fr.rob.cli.opcode

import fr.rob.cli.ConsoleApplication
import fr.rob.cli.security.auth.AuthenticationResultOpcode
import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.login.opcode.ServerOpcodeLogin

class CliOpcodeHandler(private val console: ConsoleApplication, logger: LoggerInterface) : OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(ServerOpcodeLogin.AUTHENTICATION_RESULT, AuthenticationResultOpcode(console))
    }
}
