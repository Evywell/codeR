package fr.rob.login.security.authentication.jwt

import fr.rob.core.auth.jwt.JWTDecoderInterface
import fr.rob.entities.AuthenticationProto
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.AuthenticationProcess

class JWTAuthenticationProcess(private val jwtDecoder: JWTDecoderInterface, accountProcess: AccountProcess) :
    AuthenticationProcess(accountProcess) {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        val token = (authMessage as AuthenticationProto.JWTAuthentication).token

        if (token.isEmpty()) {
            throw Exception("You MUST specify a value for the token")
        }

        val errorState = LoginAuthenticationState(false, error = ERROR_BAD_CREDENTIALS)

        return try {
            val result = jwtDecoder.decode(token)
            val accountName = result.get(JWT_AUTH_RESULT_ACCOUNT_NAME)

            if (result.get(JWT_AUTH_RESULT_USER_ID) == null) {
                return errorState
            }

            val accountId = (result.get(JWT_AUTH_RESULT_USER_ID) as String).toInt()

            errorState.accountId = accountId

            // Not a valid ticket
            if (
                result.get(JWT_AUTH_RESULT_EMAIL) == null
                || accountName == null
            ) {
                return errorState
            }

            val game: JWTResultGame =
                result.get(JWT_AUTH_RESULT_GAME, JWTResultGame::class.java) as JWTResultGame

            // Wrong game
            if (!game.slug.equals(JWT_AUTH_RESULT_GAME_SLUG)) {
                return errorState
            }

            LoginAuthenticationState(true, accountId, accountName = accountName as String)
        } catch (e: Exception) {
            errorState
        }
    }
}

const val JWT_AUTH_RESULT_EMAIL = "email"
const val JWT_AUTH_RESULT_ACCOUNT_NAME = "account"
const val JWT_AUTH_RESULT_USER_ID = "sub" // JWT standard
const val JWT_AUTH_RESULT_GAME = "game"
const val JWT_AUTH_RESULT_GAME_SLUG = "rob"

data class JWTResultGame(var slug: String? = null, var game: String? = null)
