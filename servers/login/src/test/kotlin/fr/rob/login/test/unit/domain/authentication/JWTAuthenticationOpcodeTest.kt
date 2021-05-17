package fr.rob.login.test.unit.domain.authentication

import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.core.test.unit.sandbox.network.NISession
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.jwt.JWTAuthenticationOpcode
import fr.rob.login.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.login.security.authentication.jwt.JWTResultGame
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.junit.Assert
import org.junit.Test

class JWTAuthenticationOpcodeTest : JWTBaseTest() {

    @Test
    fun `call the authentication opcode`() {
        // Arrange
        val mapping = HashMap<String, Class<*>>()
        mapping["game"] = JWTResultGame::class.java

        val jwtService = JWTDecoderService(getPublicKey(), JacksonDeserializer(mapping))

        val authProcess = JWTAuthenticationProcess(jwtService)

        val opcodeFunction = JWTAuthenticationOpcode(authProcess)
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