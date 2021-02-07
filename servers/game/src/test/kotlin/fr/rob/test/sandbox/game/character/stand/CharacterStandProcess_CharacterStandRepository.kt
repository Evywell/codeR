package fr.rob.test.sandbox.game.character.stand

import fr.rob.entities.CharacterStandProtos.CharacterStand.Character
import fr.rob.game.domain.game.character.stand.CharacterStandRepositoryInterface

class CharacterStandProcess_CharacterStandRepository : CharacterStandRepositoryInterface {

    override fun byUserId(userId: Int): List<Character> {
        val characters = ArrayList<Character>()

        characters.add(Character.newBuilder().setId(1).setName("T101").setLevel(60).build())
        characters.add(Character.newBuilder().setId(2).setName("T102").setLevel(54).build())

        return characters
    }
}