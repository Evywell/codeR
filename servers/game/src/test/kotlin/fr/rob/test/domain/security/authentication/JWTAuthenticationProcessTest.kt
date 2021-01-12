package fr.rob.test.domain.security.authentication

import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.core.security.PublicKeyReader
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.test.BaseTest
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.junit.Test
import java.io.File
import java.security.PublicKey
import java.util.*

class JWTAuthenticationProcessTest : BaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        processManager.registerProcess(AuthenticationProcess::class) {
            val jwtService = JWTDecoderService(getPublicKey(), JacksonDeserializer())

            JWTAuthenticationProcess(jwtService)
        }

        // @todo: Create a JWTEncoderService
        // @todo: Create a token with the JWTEncoderService using private.pem
        // @todo: Verify the token with the process
        // @todo: Assert the session is authenticated

        // Act


        // Assert
    }

    private fun getPublicKey(): PublicKey {
        val pkContent = File(getResourceURL("public.pem")!!
            .toURI())
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)

        return PublicKeyReader.fromString(String(Base64.getDecoder().decode(pkContent)))
    }
}
