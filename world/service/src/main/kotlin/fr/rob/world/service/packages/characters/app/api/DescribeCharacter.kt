package fr.rob.world.service.packages.characters.app.api

import fr.rob.world.service.packages.characters.domain.character.Character
import fr.rob.world.service.packages.characters.domain.character.CharacterId
import fr.rob.world.service.packages.characters.domain.character.CharacterRepositoryInterface
import fr.rob.world.service.packages.characters.domain.character.exception.CharacterDoesNotExistException

class DescribeCharacter(private val characterRepository: CharacterRepositoryInterface) {
    fun invoke(characterId: CharacterId): CharacterDescriptionResponse {
        return try {
            val character = characterRepository.retrieveById(characterId)

            CharacterDescriptionResponse(character)
        } catch (characterDoesNotExistException: CharacterDoesNotExistException) {
            CharacterDescriptionResponse(error = characterDoesNotExistException.message)
        }
    }

    data class CharacterDescriptionResponse(val description: Character? = null, val error: String? = null)
}
