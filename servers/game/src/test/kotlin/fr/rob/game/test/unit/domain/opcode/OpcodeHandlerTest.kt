package fr.rob.game.test.unit.domain.opcode

import fr.rob.core.test.unit.BaseTest
import fr.rob.core.network.Packet
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.core.security.authentication.UnauthenticatedException
import fr.rob.game.sandbox.SandboxProtos
import fr.rob.game.test.unit.sandbox.opcode.HandlingOpcodeWithProtoAsMessageOpcode
import fr.rob.game.test.unit.sandbox.network.EmptyPacket
import fr.rob.game.test.unit.sandbox.opcode.DoNothingAuthenticatedOpcodeFunction
import fr.rob.game.test.unit.sandbox.opcode.DoNothingOpcodeFunction
import fr.rob.game.test.unit.sandbox.opcode.TransformToSarahConnorOpcodeFunction
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OpcodeHandlerTest : BaseTest() {

    @Test
    fun `ensure the right opcode function is proceed`() {
        // Arrange
        val session = NISession()
        val packet = EmptyPacket()

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
        val session = NISession()
        
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

    @Test
    fun `launch opcode function with unauthenticated session`() {
        // Arrange
        val session = NISession()
        val opcodeFunction = DoNothingAuthenticatedOpcodeFunction(BasicSubject("a", "a", 18))

        opcodeHandler.registerOpcode(1, opcodeFunction)

        // Act & Assert
        Assertions.assertThrows(UnauthenticatedException::class.java) {
            opcodeHandler.process(1, session, Packet(1, null))
            assertEquals(false, session.isAuthenticated)
        }
    }

    @Test
    fun `process an unregistered opcode`() {
        // Act & Assert
        val exception = Assertions.assertThrows(Exception::class.java) {
            opcodeHandler.process(1, NISession(), Packet(1, null))
        }

        assertEquals("Cannot find opcode 1", exception.message)
    }

    @Test
    fun `get an opcode function`() {
        // Arrange
        val opcodeFunction = DoNothingAuthenticatedOpcodeFunction(BasicSubject("a", "a", 18))

        opcodeHandler.registerOpcode(1, opcodeFunction)

        // Act & Assert
        assertEquals(opcodeFunction, opcodeHandler.getOpcodeFunction(1));
    }
}

data class BasicSubject(
    var firstName: String,
    var lastName: String,
    var age: Int
)
