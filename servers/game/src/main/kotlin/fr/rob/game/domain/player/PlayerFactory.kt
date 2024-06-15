package fr.rob.game.domain.player

import fr.rob.game.domain.character.CharacterService
import fr.rob.game.domain.character.FetchCharacterInterface
import fr.rob.game.domain.combat.CombatTrait
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.InstantAoeDamageEffect

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
        // player.addTrait(MovableTrait(player))
        player.addTrait(Movable(player))
        player.addTrait(CombatTrait(player))
        // @todo change this
        player.addTrait(
            SpellCasterTrait(
                player,
                SpellBook(
                    hashMapOf(
                        1 to SpellInfo(
                            1,
                            SpellInfo.LaunchType.INSTANT,
                            arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(1, 5f)),
                        ),
                    ),
                ),
            ),
        )

        return PlayerInitResult(true, player, character.position)
    }

    data class PlayerInitResult(
        val isSuccess: Boolean,
        val player: Player? = null,
        val position: Position? = null,
    )
}
