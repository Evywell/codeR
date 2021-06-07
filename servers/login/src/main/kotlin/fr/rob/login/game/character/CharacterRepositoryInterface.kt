package fr.rob.login.game.character

import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterProtos.Character

interface CharacterRepositoryInterface {

    fun isCharacterNameTaken(characterName: String): Boolean
    fun insert(accountId: Int, characterSkeleton: CharacterCreateProtos.CharacterCreate, level: Int = DEFAULT_MIN_LEVEL): Character
    fun setCurrentCharacter(character: Character)

    fun allByAccountId(accountId: Int): MutableList<Character>

    companion object {
        const val DEFAULT_MIN_LEVEL = 1
    }
}
