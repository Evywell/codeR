package fr.rob.login.game.character

import fr.rob.entities.CharacterProtos.Character

interface CharactersHolderInterface {

    fun getCharactersNumber(): Int
    fun getCharacterByName(characterName: String): Character?
}
