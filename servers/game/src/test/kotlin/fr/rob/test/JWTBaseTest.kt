package fr.rob.test

import fr.rob.core.auth.jwt.JWTDecoderService
import fr.rob.core.security.PublicKeyReader
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTAuthenticationProcess
import fr.rob.game.domain.security.authentication.jwt.JWTResultGame
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import org.bouncycastle.util.io.pem.PemReader
import java.io.File
import java.io.FileReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

open class JWTBaseTest : BaseTest() {

    fun registerJWTProcess(processManager: ProcessManager) {
        processManager.registerProcess(AuthenticationProcess::class) {
            val mapping = HashMap<String, Class<*>>()
            mapping["game"] = JWTResultGame::class.java

            val jwtService = JWTDecoderService(getPublicKey(), JacksonDeserializer(mapping))

            JWTAuthenticationProcess(jwtService)
        }
    }

    protected fun generateJWT(userId: Int, email: String, game: JWTResultGame): String {
        val localDate = LocalDate.now().plusDays(1)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setId("ad2fdd4c-2df1-44a5-89da-0092f88c1127")
            .setIssuedAt(Date())
            .setExpiration(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .claim("email", email)
            .claim("game", game)
            .signWith(getPrivateKey())
            .compact()
    }

    private fun getPublicKey(): PublicKey {
        return PublicKeyReader.fromString(getKeyContent("public.pem"))
    }

    private fun getKeyContent(filename: String): String {
        return getKey(filename)
            .inputStream()
            .readBytes()
            .toString(Charsets.UTF_8)
    }

    private fun getPrivateKey(): PrivateKey {
        val reader = PemReader(FileReader(getKey("private.pem")))
        val spec = PKCS8EncodedKeySpec(reader.readPemObject().content)

        return KeyFactory.getInstance("RSA").generatePrivate(spec)
    }

    private fun getKey(filename: String): File {
        return File(
            getResourceURL(filename)!!
                .toURI()
        )
    }
}