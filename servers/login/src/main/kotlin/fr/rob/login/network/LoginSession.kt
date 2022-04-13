package fr.rob.login.network

import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharactersHolderInterface
import fr.rob.login.security.account.Account
import fr.rob.login.security.exception.SessionNotOperatorException

open class LoginSession(socket: SessionSocketInterface) : Session(socket), CharactersHolderInterface {

    lateinit var account: Account
    lateinit var characters: MutableList<Character>

    override fun getCharactersNumber(): Int = characters.size

    override fun getCharacterByName(characterName: String): Character? {
        for (character in characters) {
            if (character.name.equals(characterName)) {
                return character
            }
        }

        return null
    }

    fun isOperatorOrThrowException() {
        if (!isOperator()) {
            throw SessionNotOperatorException()
        }
    }

    fun isOperator(): Boolean =
        isAuthenticated
            && account.isAdministrator
            && isLocal()
}
