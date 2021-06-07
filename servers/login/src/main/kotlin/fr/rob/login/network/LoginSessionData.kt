package fr.rob.login.network

import fr.rob.core.network.session.SessionData
import fr.rob.entities.AccountProto.Account
import fr.rob.entities.CharacterProtos.Character
import fr.rob.login.game.character.CharactersHolderInterface

class LoginSessionData : SessionData, CharactersHolderInterface {

    lateinit var account: Account
    var characters: MutableList<Character>? = null

    override fun getCharactersNumber(): Int = characters?.size ?: 0

    override fun getCharacterByName(characterName: String): Character? {
        if (characters == null) {
            return null
        }

        for (character in characters!!) {
            if (character.name.equals(characterName)) {
                return character
            }
        }

        return null
    }
}
