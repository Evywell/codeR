package fr.rob.login.security.authentication.jwt

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.authentication.AuthenticationProcess

class JWTAuthenticationProcess(private val jwtDecoder: JWTDecoderInterface) : AuthenticationProcess() {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        val token = (authMessage as AuthenticationProto.JWTAuthentication).token

        if (token.isEmpty()) {
            throw Exception("You MUST specify a value for the token")
        }

        val errorState = AuthenticationState(false, error = ERROR_BAD_CREDENTIALS)

        return try {
            val result = jwtDecoder.decode(token)

            // Not a valid ticket
            if (
                result.get(JWT_AUTH_RESULT_EMAIL) == null
                || result.get(JWT_AUTH_RESULT_USER_ID) == null
            ) {
                return errorState
            }

            val game: JWTResultGame =
                result.get(JWT_AUTH_RESULT_GAME, JWTResultGame::class.java) as JWTResultGame

            // Wrong game
            if (!game.slug.equals(JWT_AUTH_RESULT_GAME_SLUG)) {
                return errorState
            }

            val userId = (result.get(JWT_AUTH_RESULT_USER_ID) as String).toInt()

            AuthenticationState(true, userId)
        } catch (e: Exception) {
            errorState
        }
    }
}

const val JWT_AUTH_RESULT_EMAIL = "email"
const val JWT_AUTH_RESULT_USER_ID = "sub" // JWT standard
const val JWT_AUTH_RESULT_GAME = "game"
const val JWT_AUTH_RESULT_GAME_SLUG = "rob"

data class JWTResultGame(var slug: String? = null, var game: String? = null)
