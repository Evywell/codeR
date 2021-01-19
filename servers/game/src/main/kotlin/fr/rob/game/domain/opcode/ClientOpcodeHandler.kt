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
        registerOpcode(
            OpcodeClient.AUTHENTICATE_SESSION,
            if (app.env === ENV_DEV) DevAuthenticationOpcode(processManager) else JWTAuthenticationOpcode(processManager)
        )
    }
}
