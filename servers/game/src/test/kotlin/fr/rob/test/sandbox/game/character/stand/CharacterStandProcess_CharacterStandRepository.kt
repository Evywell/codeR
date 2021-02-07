package fr.rob.test.sandbox.game.character.stand

import fr.rob.entities.CharacterStandProtos.CharacterStand.Character
import fr.rob.game.domain.game.character.stand.CharacterStandRepositoryInterface

class CharacterStandProcess_CharacterStandRepository : CharacterStandRepositoryInterface {

    private val characters = ArrayList<Character>()

    init {
        characters.add(Character.newBuilder().setId(13).setName("T101").setLevel(60).build())
        characters.add(Character.newBuilder().setId(22).setName("T102").setLevel(54).build())
    }

    override fun byUserId(userId: Int): List<Character> {
        return characters
    }

    override fun getCurrentCharacterId(userId: Int): Int {
        return characters[0].id
    }
}