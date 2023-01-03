package fr.rob.world.service.packages.characters.infra.repository.characters

import fr.rob.world.service.packages.characters.domain.character.Character
import fr.rob.world.service.packages.characters.domain.character.CharacterId
import fr.rob.world.service.packages.characters.domain.character.CharacterRepositoryInterface

class MySQLCharacterRepository : CharacterRepositoryInterface {
    override fun retrieveById(characterId: CharacterId): Character {
        // TODO: replace by real data
        return Character(CharacterId(1), "Evywell", 1, 1, 1)
    }
}
