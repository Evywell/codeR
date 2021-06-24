package fr.rob.login.test.unit.sandbox.game.character.create

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepositoryInterface

class CharacterCreateProcess_CharacterRepository : CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean = false

    override fun insert(
        accountId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): Character = Character(null, level, characterSkeleton.name)

    override fun setCurrentCharacter(character: Character) {}

    override fun allByAccountId(accountId: Int): MutableList<Character> {
        TODO("Not yet implemented")
    }
}

class CharacterCreateProcess_CharacterRepository2 : CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean = true

    override fun insert(
        accountId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): Character = Character(null, level, characterSkeleton.name)

    override fun setCurrentCharacter(character: Character) {}

    override fun allByAccountId(accountId: Int): MutableList<Character> {
        TODO("Not yet implemented")
    }
}
