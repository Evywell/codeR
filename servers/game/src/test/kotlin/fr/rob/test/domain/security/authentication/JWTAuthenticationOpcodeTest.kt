package fr.rob.test.domain.security.authentication

import fr.rob.entities.AuthenticationProto
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.game.domain.security.authentication.jwt.JWTResultGame
import fr.rob.test.JWTBaseTest
import fr.rob.test.sandbox.network.NISession
import org.junit.Assert
import org.junit.Test

class JWTAuthenticationOpcodeTest : JWTBaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        registerJWTProcess(processManager)

        val opcodeFunction = JWTAuthenticationOpcode(processManager)
        val session = NISession(getGameServer())

        val jwt = generateJWT(123456, "player@localhost", JWTResultGame("rob", "Rob"))

        val message = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        opcodeHandler.registerOpcode(1, opcodeFunction)
        opcodeFunction.call(session, message)

        // Assert
        Assert.assertEquals(true, session.isAuthenticated)
        Assert.assertEquals(123456, session.userId)
    }
}
