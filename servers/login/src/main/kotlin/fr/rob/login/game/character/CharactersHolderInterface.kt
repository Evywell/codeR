package fr.rob.login.game.character

interface CharactersHolderInterface {

    fun getCharactersNumber(): Int
    fun getCharacterByName(characterName: String): Character?
}
