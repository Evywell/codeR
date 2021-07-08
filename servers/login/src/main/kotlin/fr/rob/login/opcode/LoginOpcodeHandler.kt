package fr.rob.login.opcode

import fr.rob.core.ENV_DEV
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.AdvancedOpcodeHandlerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.create.CharacterCreateOpcode
import fr.rob.login.game.character.stand.CharacterStandOpcode
import fr.rob.login.security.authentication.AuthenticationProcess
import fr.rob.login.security.authentication.dev.DevAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.strategy.ChangeStrategyOpcode

class LoginOpcodeHandler(
    private val env: String,
    override var processManager: ProcessManager,
    private val eventManager: EventManagerInterface,
    logger: LoggerInterface
) :
    OpcodeHandler(logger), AdvancedOpcodeHandlerInterface {

    override fun initialize() {
        this.registerAuthenticationOpcode()

        registerAutowiredOpcode(ClientOpcodeLogin.CHARACTER_STAND, CharacterStandOpcode::class)
        registerAutowiredOpcode(ClientOpcodeLogin.CHARACTER_CREATE, CharacterCreateOpcode::class)

        registerAutowiredOpcode(ClientOpcodeLogin.OPERATOR_CHANGE_STRATEGY, ChangeStrategyOpcode::class)
    }

    private fun registerAuthenticationOpcode() {
        val authenticationProcess = processManager.makeProcess(AuthenticationProcess::class)
        val sessionInitializerProcess = processManager.makeProcess(SessionInitializerProcess::class)

        registerOpcode(
            ClientOpcodeLogin.AUTHENTICATE_SESSION,
            if (env == ENV_DEV)
                DevAuthenticationOpcode(authenticationProcess, sessionInitializerProcess, eventManager)
            else
                JWTAuthenticationOpcode(authenticationProcess, sessionInitializerProcess, eventManager)
        )
    }
}
