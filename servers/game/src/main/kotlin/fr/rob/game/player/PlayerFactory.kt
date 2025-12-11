package fr.rob.game.player

import fr.rob.game.character.CharacterService
import fr.rob.game.character.FetchCharacterInterface
import fr.rob.game.combat.CombatTrait
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.movement.Movable
import fr.rob.game.player.session.GameSession
import fr.rob.game.spell.SpellBook
import fr.rob.game.spell.SpellCasterTrait
import fr.rob.game.spell.SpellInfo
import fr.rob.game.spell.effect.InstantAoeDamageEffect
import fr.rob.game.spell.trigger.ApplyEffectsSpellTrigger
import fr.rob.game.spell.type.instant.InstantLaunchInfo

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
                            InstantLaunchInfo(ApplyEffectsSpellTrigger(arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(1, 5f))))
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
