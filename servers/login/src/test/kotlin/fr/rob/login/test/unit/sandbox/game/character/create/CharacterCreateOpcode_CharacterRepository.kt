package fr.rob.login.test.unit.sandbox.game.character.create

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepositoryInterface

class CharacterCreateOpcode_CharacterRepository : CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean = false

    override fun insert(
        accountId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): Character = Character(1, level, characterSkeleton.name)

    override fun setCurrentCharacter(character: Character) { }

    override fun allByAccountId(accountId: Int): MutableList<Character> {
        TODO("Not yet implemented")
    }
}
