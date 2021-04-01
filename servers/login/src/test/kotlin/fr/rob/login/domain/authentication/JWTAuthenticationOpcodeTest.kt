package fr.rob.login.domain.authentication

import fr.rob.core.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.JWTBaseTest
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTResultGame
import org.junit.Assert
import org.junit.Test

class JWTAuthenticationOpcodeTest : JWTBaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        registerJWTProcess(processManager)

        val opcodeFunction = JWTAuthenticationOpcode(processManager)
        val session = NISession()

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
