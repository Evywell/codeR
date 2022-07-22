package fr.rob.game.domain.character

interface FetchCharacterInterface {
    /**
     * @exception fr.rob.game.game.world.character.exception.CharacterNotFoundException
     */
    fun retrieveCharacter(id: Int): Character
}
