package fr.rob.game.domain.security.authentication.jwt

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.game.domain.security.authentication.AuthenticationProcess
import fr.rob.game.entity.authentication.AuthenticationProto

class JWTAuthenticationProcess(private val jwtDecoder: JWTDecoderInterface) : AuthenticationProcess() {

    var userId: Int? = null

    override fun checkAuthentication(authMessage: Any): Boolean {
        val token = (authMessage as AuthenticationProto.JWTAuthentication).token

        if (token.isEmpty()) {
            throw Exception("You MUST specify a value for the token")
        }

        return try {
            val result = jwtDecoder.decode(token)

            // Not a valid ticket
            if (
                result.get(JWT_AUTH_RESULT_EMAIL) == null
                || result.get(JWT_AUTH_RESULT_USER_ID) == null
            ) {
                return false
            }

            val game: JWTResultGame =
                result.get(JWT_AUTH_RESULT_GAME, JWTResultGame::class.java) as JWTResultGame

            // Wrong game
            if (!game.slug.equals(JWT_AUTH_RESULT_GAME_SLUG)) {
                return false
            }

            userId = (result.get(JWT_AUTH_RESULT_USER_ID) as String).toInt()

            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getUserId(): Int = userId!!
}

const val JWT_AUTH_RESULT_EMAIL = "email"
const val JWT_AUTH_RESULT_USER_ID = "sub" // JWT standard
const val JWT_AUTH_RESULT_GAME = "game"
const val JWT_AUTH_RESULT_GAME_SLUG = "rob"

data class JWTResultGame(var slug: String? = null, var game: String? = null)
