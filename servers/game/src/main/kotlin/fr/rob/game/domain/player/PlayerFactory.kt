package fr.rob.game.domain.player

import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.FetchCharacterInterface
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.session.GameSession

class PlayerFactory(
    private val characterService: CharacterService,
    private val fetchCharacter: FetchCharacterInterface,
    private val guidGenerator: ObjectGuidGenerator
) {
    fun createFromGameSession(session: GameSession, characterId: Int, mapInstance: MapInstance): PlayerInitResult {
        if (!characterService.checkCharacterBelongsToAccount(characterId, session.accountId)) {
            return PlayerInitResult(false)
        }

        val character = fetchCharacter.retrieveCharacter(characterId)
        val guid = guidGenerator.createForPlayer(character.id)

        val player = Player(session, guid, character.name, character.level)
        player.position = character.position
        player.mapInstance = mapInstance

        return PlayerInitResult(true, player)
    }

    data class PlayerInitResult(
        val isSuccess: Boolean,
        val player: Player? = null
    )
}
