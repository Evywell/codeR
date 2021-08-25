package fr.rob.orchestrator.test.unit.opcode

import fr.rob.orchestrator.opcode.OrchestratorOpcodeHandler
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator
import fr.rob.orchestrator.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.test.unit.BaseTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OrchestratorOpcodeHandlerTest : BaseTest() {

    @Test
    fun `as an opcode handle I should load all the opcodes successfully`() {
        // Arrange
        val opcodeHandler = OrchestratorOpcodeHandler(processManager, logger)

        // Act 
        opcodeHandler.initialize()

        val function = opcodeHandler.getOpcodeFunction(ServerOpcodeOrchestrator.AUTHENTICATE_SESSION)

        // Assert
        assertTrue(function is AuthenticationOpcode)
    }
}
