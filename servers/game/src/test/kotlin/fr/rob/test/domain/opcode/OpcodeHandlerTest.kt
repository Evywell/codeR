package fr.rob.test.domain.opcode

import fr.rob.game.domain.opcode.OpcodeHandler
import fr.rob.game.domain.server.GameServer
import fr.rob.game.domain.server.packet.Packet
import fr.rob.game.sandbox.SandboxProtos
import fr.rob.test.BaseTest
import fr.rob.test.opcode.HandlingOpcodeWithProtoAsMessageOpcode
import fr.rob.test.sandbox.network.EmptyPacket
import fr.rob.test.sandbox.network.NISession
import fr.rob.test.sandbox.opcode.DoNothingOpcodeFunction
import fr.rob.test.sandbox.opcode.TransformToSarahConnorOpcodeFunction
import org.junit.Assert.assertEquals
import org.junit.Test

class OpcodeHandlerTest : BaseTest() {

    @Test
    fun `ensure the right opcode function is proceed`() {
        // Arrange
        val session = NISession(GameServer(logger))
        val packet = EmptyPacket()

        val opcodeHandler = OpcodeHandler(logger)
        val subject1 = BasicSubject("John", "Connor", 14)
        val subject2 = BasicSubject("John", "Connor", 15)

        // Act
        opcodeHandler.registerOpcode(1, TransformToSarahConnorOpcodeFunction(subject1))
        opcodeHandler.registerOpcode(2, DoNothingOpcodeFunction(subject2))

        opcodeHandler.process(1, session, packet)
        opcodeHandler.process(2, session, packet)

        // Assert
        assertEquals("Sarah", subject1.firstName)
        assertEquals("Connor", subject1.lastName)
        assertEquals(28, subject1.age)

        assertEquals("John", subject2.firstName)
        assertEquals("Connor", subject2.lastName)
        assertEquals(15, subject2.age)
    }

    @Test
    fun `handling opcode with proto as message`() {
        // Arrange
        val opcode = 1
        val gs = GameServer(logger)
        val session = NISession(gs)
        val opcodeHandler = OpcodeHandler(logger)

        val subject = SandboxProtos.Subject.newBuilder()
            .setFirstName("John")
            .setLastName("Connor")
            .setAge(15)
            .build()

        val packet = Packet(opcode, subject.toByteArray())

        val opcodeFunction = HandlingOpcodeWithProtoAsMessageOpcode()

        // Act
        opcodeHandler.registerOpcode(opcode, opcodeFunction)
        opcodeHandler.process(opcode, session, packet)

        val sarahConnor = opcodeFunction.sarahConnor

        // Assert
        assertEquals("Sarah", sarahConnor.firstName)
        assertEquals("Connor", sarahConnor.lastName)
        assertEquals(32, sarahConnor.age)
    }
}

data class BasicSubject(
    var firstName: String,
    var lastName: String,
    var age: Int
)