package fr.rob.world.service.packages.characters.domain.character.exception

import fr.rob.world.service.packages.characters.domain.character.CharacterId
import fr.rob.world.service.packages.common.domain.exception.DomainException

class CharacterDoesNotExistException : DomainException {
    constructor(characterId: CharacterId) : super("The character with id ${characterId.value} does not exist")
}
