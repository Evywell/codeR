package fr.rob.login.test.unit.sandbox.game.character.stand

import fr.rob.entities.CharacterCreateProtos
import fr.rob.entities.CharacterProtos
import fr.rob.login.game.character.CharacterRepositoryInterface

class SessionInitializerProcess_CharacterRepository(private val characters: MutableList<CharacterProtos.Character>) :
    CharacterRepositoryInterface {

    override fun isCharacterNameTaken(characterName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun insert(
        userId: Int,
        characterSkeleton: CharacterCreateProtos.CharacterCreate,
        level: Int
    ): CharacterProtos.Character {
        TODO("Not yet implemented")
    }

    override fun setCurrentCharacter(character: CharacterProtos.Character) {
        TODO("Not yet implemented")
    }

    override fun allByUserId(userId: Int): MutableList<CharacterProtos.Character> = characters
}
