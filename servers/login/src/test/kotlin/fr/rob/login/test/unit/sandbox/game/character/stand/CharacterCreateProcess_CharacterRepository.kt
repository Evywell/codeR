package fr.rob.login.test.unit.sandbox.game.character.stand

import fr.rob.entities.CharacterCreateProtos
import fr.rob.login.game.character.Character
import fr.rob.login.game.character.CharacterRepositoryInterface

class CharacterCreateProcess_CharacterRepository : CharacterRepositoryInterface {
    override fun isCharacterNameTaken(characterName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun insert(
        accountId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): Character {
        TODO("Not yet implemented")
    }

    override fun setCurrentCharacter(character: Character) {
        TODO("Not yet implemented")
    }

    override fun allByAccountId(accountId: Int): MutableList<Character> {
        TODO("Not yet implemented")
    }
}
