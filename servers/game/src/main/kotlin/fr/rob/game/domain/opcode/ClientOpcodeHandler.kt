package fr.rob.game.domain.opcode

import fr.rob.core.BaseApplication
import fr.rob.game.domain.game.OpcodeClient
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationOpcode

class ClientOpcodeHandler(private val processManager: ProcessManager, app: BaseApplication, logger: LoggerInterface) :
    OpcodeHandler(app, logger) {

    override fun initialize() {
        registerOpcode(OpcodeClient.AUTHENTICATE_SESSION, AuthenticationOpcode(app.env, processManager))
    }
}
