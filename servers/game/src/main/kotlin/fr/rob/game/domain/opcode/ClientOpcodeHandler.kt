package fr.rob.game.domain.opcode

import fr.rob.core.log.LoggerInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.process.ProcessManager
import fr.rob.game.domain.game.OpcodeClient
import fr.rob.game.domain.game.character.stand.CharacterStandOpcode

class ClientOpcodeHandler(private val processManager: ProcessManager, logger: LoggerInterface) :
    OpcodeHandler(logger) {

    override fun initialize() {
        registerOpcode(OpcodeClient.CHARACTER_STAND, CharacterStandOpcode(processManager))
    }
}
