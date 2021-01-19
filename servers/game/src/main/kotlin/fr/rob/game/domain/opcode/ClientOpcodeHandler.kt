package fr.rob.game.domain.opcode

import fr.rob.core.BaseApplication
import fr.rob.game.ENV_DEV
import fr.rob.game.domain.game.OpcodeClient
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationOpcode

class ClientOpcodeHandler(private val processManager: ProcessManager, app: BaseApplication, logger: LoggerInterface) :
    OpcodeHandler(app, logger) {

    override fun initialize() {
        registerAuthenticationOpcode()
    }

    private fun registerAuthenticationOpcode() {
        if (app.env === ENV_DEV) {
            registerOpcode(OpcodeClient.AUTHENTICATE_SESSION, DevAuthenticationOpcode(processManager))
        } else {
            registerOpcode(OpcodeClient.AUTHENTICATE_SESSION, JWTAuthenticationOpcode(processManager))
        }
    }
}
