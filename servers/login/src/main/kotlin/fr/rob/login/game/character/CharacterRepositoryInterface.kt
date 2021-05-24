package fr.rob.login.game.character

import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterProtos.Character

interface CharacterRepositoryInterface {

    fun isCharacterNameTaken(characterName: String): Boolean
    fun insert(userId: Int, characterSkeleton: CharacterCreateProtos.CharacterCreate): Character
    fun setCurrentCharacter(character: Character)

    fun allByUserId(userId: Int): MutableList<Character>
}
