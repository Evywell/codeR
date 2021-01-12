package fr.rob.game.domain.authentication.jwt

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.game.domain.authentication.AuthenticationProcess
import java.lang.Exception

/**
 * @todo: continue in another feature, please ignore
 */
class JWTAuthenticationProcess(private val jwtDecoder: JWTDecoderInterface) : AuthenticationProcess() {

    private lateinit var token: String

    override fun checkAuthentication(): Boolean {
        if (token.isEmpty()) {
            throw Exception("You MUST specify a value for the token")
        }

        return try {
            val result = jwtDecoder.decode(token)

            // Not a valid ticket
            if (result.get(JWT_AUTH_RESULT_EMAIL) == null) {
                return false
            }

            val game: JWTResultGame =
                result.get(JWT_AUTH_RESULT_GAME, JWTResultGame::class.java) as JWTResultGame

            // Wrong game
            if (!game.slug.equals(JWT_AUTH_RESULT_GAME_SLUG)) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }
}

const val JWT_AUTH_RESULT_EMAIL = "email"
const val JWT_AUTH_RESULT_GAME = "game"
const val JWT_AUTH_RESULT_GAME_SLUG = "rob"

data class JWTResultGame(val slug: String?, val game: String?)
