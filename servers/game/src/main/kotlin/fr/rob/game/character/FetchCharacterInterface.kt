package fr.rob.game.character

interface FetchCharacterInterface {
    /**
     * @exception fr.rob.game.game.world.character.exception.CharacterNotFoundException
     */
    fun retrieveCharacter(id: Int): Character
}
