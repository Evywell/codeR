package fr.rob.game.domain.player

import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.FetchCharacterInterface
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.behavior.MovableTrait
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.player.session.GameSession

class PlayerFactory(
    private val characterService: CharacterService,
    private val fetchCharacter: FetchCharacterInterface,
    private val guidGenerator: ObjectGuidGenerator,
) {
    fun createFromGameSession(session: GameSession, characterId: Int): PlayerInitResult {
        if (!characterService.checkCharacterBelongsToAccount(characterId, session.accountId)) {
            return PlayerInitResult(false)
        }

        val character = fetchCharacter.retrieveCharacter(characterId)
        val guid = guidGenerator.createForPlayer(character.id)

        val player = Player(session, guid, character.name, character.level)
        player.addTrait(MovableTrait(player))

        return PlayerInitResult(true, player, character.position)
    }

    data class PlayerInitResult(
        val isSuccess: Boolean,
        val player: Player? = null,
        val position: Position? = null,
    )
}
