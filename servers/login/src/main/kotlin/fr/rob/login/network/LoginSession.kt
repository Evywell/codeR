package fr.rob.login.network

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.session.Session
import fr.rob.entities.AccountProto
import fr.rob.entities.CharacterProtos
import fr.rob.login.game.character.CharactersHolderInterface
import fr.rob.login.security.exception.SessionNotOperatorException

open class LoginSession(private val logger: LoggerInterface) : Session(), CharactersHolderInterface {

    lateinit var account: AccountProto.Account
    lateinit var characters: MutableList<CharacterProtos.Character>

    override fun getCharactersNumber(): Int = characters.size

    override fun getCharacterByName(characterName: String): CharacterProtos.Character? {
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
