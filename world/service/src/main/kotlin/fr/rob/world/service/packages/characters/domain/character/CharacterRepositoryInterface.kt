package fr.rob.world.service.packages.characters.domain.character

interface CharacterRepositoryInterface {
    /**
     * @throws fr.rob.world.service.packages.characters.domain.character.exception.CharacterDoesNotExistException
     */
    fun retrieveById(characterId: CharacterId): Character
}
