package fr.rob.game.domain.authentication

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.game.domain.network.Session
import java.lang.Exception

/**
 * @todo: continue in another feature, please ignore
 */
class AuthenticationProcess(private val jwtDecoder: JWTDecoderInterface) {

    fun authenticateByJWT(token: String, session: Session): Boolean {
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

            session.authenticated = true

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
