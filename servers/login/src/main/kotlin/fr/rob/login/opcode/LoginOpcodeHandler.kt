package fr.rob.login.opcode

import fr.rob.core.ENV_DEV
import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode

class LoginOpcodeHandler(private val env: String, private val processManager: ProcessManager, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(
            OpcodeLogin.AUTHENTICATE_SESSION,
            if (env == ENV_DEV) DevAuthenticationOpcode(processManager) else JWTAuthenticationOpcode(processManager)
        )
    }
}