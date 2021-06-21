package fr.rob.login.test.unit.domain.authentication

import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTResultGame
import fr.rob.login.test.unit.sandbox.network.LoginSessionFactory
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

class JWTAuthenticationOpcodeTest : JWTBaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        val sessionInitializerProcess = getSessionInitializerProcessMock()

        val mapping = HashMap<String, Class<*>>()
        mapping["game"] = JWTResultGame::class.java

        val jwtService = JWTDecoderService(getPublicKey(), JacksonDeserializer(mapping))

        val authProcess = JWTAuthenticationProcess(jwtService)

        val opcodeFunction = JWTAuthenticationOpcode(authProcess, sessionInitializerProcess)
        val session = LoginSessionFactory.buildSession()

        val jwt = generateJWT(123456, "player@localhost", JWTResultGame("rob", "Rob"), ACCOUNT_NAME_1)

        val message = AuthenticationProto.JWTAuthentication.newBuilder()
            .setToken(jwt)
            .build()

        // Act
        opcodeHandler.registerOpcode(1, opcodeFunction)
        opcodeFunction.call(session, message)

        // Assert
        assertEquals(true, session.isAuthenticated)
        assertEquals(123456, session.userId)
        assertTrue(opcodeFunction.getMessageType() is AuthenticationProto.JWTAuthentication)
    }
}
