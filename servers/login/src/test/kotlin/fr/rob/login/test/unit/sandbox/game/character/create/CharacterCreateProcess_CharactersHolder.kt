package fr.rob.login.test.unit.sandbox.game.character.create

import fr.rob.entities.CharacterProtos
import fr.rob.login.game.character.CharactersHolderInterface

class CharacterCreateProcess_CharactersHolder : CharactersHolderInterface {

    override fun getCharactersNumber(): Int = 0

    override fun getCharacterByName(characterName: String): CharacterProtos.Character? = null
}

class CharacterCreateProcess_CharactersHolder2 : CharactersHolderInterface {

    override fun getCharactersNumber(): Int = 1

    override fun getCharacterByName(characterName: String): CharacterProtos.Character? =
        CharacterProtos.Character.getDefaultInstance()
}

class CharacterCreateProcess_CharactersHolder3 : CharactersHolderInterface {

    override fun getCharactersNumber(): Int = 10

    override fun getCharacterByName(characterName: String): CharacterProtos.Character? = null
}
