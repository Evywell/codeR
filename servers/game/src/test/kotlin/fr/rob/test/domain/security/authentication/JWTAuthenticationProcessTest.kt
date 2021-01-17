package fr.rob.test.domain.security.authentication

import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.core.security.KeyReader
import fr.rob.core.security.PrivateKeyReader
import fr.rob.core.security.PublicKeyReader
import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTResultGame
import fr.rob.test.BaseTest
import fr.rob.test.sandbox.network.NISession
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.junit.Test
import java.io.File
import java.security.PublicKey
import java.util.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.lang.Maps
import org.bouncycastle.jce.provider.PEMUtil
import org.bouncycastle.util.io.pem.PemReader
import org.junit.Assert.assertEquals
import java.io.FileReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.collections.HashMap
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


class JWTAuthenticationProcessTest : BaseTest() {

    @Test
    fun `authenticate a valid token`() {
        // Arrange
        val processManager = ProcessManager()

        processManager.registerProcess(AuthenticationProcess::class) {
            val mapping = HashMap<String, Class<*>>()
            mapping["game"] = JWTResultGame::class.java

            val jwtService = JWTDecoderService(getPublicKey(), JacksonDeserializer(mapping))

            JWTAuthenticationProcess(jwtService)
        }

        // @todo: Create a JWTEncoderService
        // @todo: Create a token with the JWTEncoderService using private.pem
        // @todo: Verify the token with the process
        // @todo: Assert the session is authenticated
        // @link https://general.support.brightcove.com/developer/create-jwt.html ?? Créer des JWT avec bash
        // @link https://www.jsonwebtoken.io/


        val localDate = LocalDate.now().plusDays(1)

        val s = Jwts.builder()
            .setSubject("1234567890")
            .setId("ad2fdd4c-2df1-44a5-89da-0092f88c1127")
            .setIssuedAt(Date())
            .setExpiration(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .claim("email", "player@example.com")
            .claim("game", JWTResultGame("rob", "Rob"))
            .signWith(getPrivateKey())
            .compact()

        // Act
        val authenticationProcess: JWTAuthenticationProcess =
            processManager.makeProcess(AuthenticationProcess::class) as JWTAuthenticationProcess
        authenticationProcess.token = s

        // Assert
        assertEquals(true, authenticationProcess.authenticate(NISession(getGameServer())))
    }

    private fun getPublicKey(): PublicKey {
        return PublicKeyReader.fromString(getKeyContent("public.pem"))
    }

    private fun getPrivateKey(): PrivateKey {
        val reader = PemReader(FileReader(getKey("private.pem")))
        val spec = PKCS8EncodedKeySpec(reader.readPemObject().content)

        return KeyFactory.getInstance("RSA").generatePrivate(spec)
    }

    private fun getKeyContent(filename: String): String {
        return getKey(filename)
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
    }

    private fun getKey(filename: String): File {
        return File(
            getResourceURL(filename)!!
                .toURI()
        )
    }
}
