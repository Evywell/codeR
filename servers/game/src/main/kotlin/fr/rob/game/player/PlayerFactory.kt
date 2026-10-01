package fr.rob.game.player

import fr.rob.game.ability.AbilityBehavior
import fr.rob.game.ability.ObjectAbilityManager
import fr.rob.game.behavior.CombatBehavior
import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.component.CombatComponent
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.player.session.GameSession
import fr.rob.game.spell.SpellBook
import fr.rob.game.spell.SpellCasterTrait
import fr.rob.game.spell.SpellInfo
import fr.rob.game.spell.effect.InstantAoeDamageEffect
import fr.rob.game.spell.trigger.ApplyEffectsSpellTrigger
import fr.rob.game.spell.type.instant.InstantLaunchInfo
import fr.rob.game.character.Character

class PlayerFactory(
    private val guidGenerator: ObjectGuidGenerator,
    private val objectAbilityManager: ObjectAbilityManager? = null,
) {
    fun createFromCharacterForSession(
        session: GameSession,
        character: Character,
    ): PlayerInitResult {
        val guid = guidGenerator.createForPlayer(character.id)

        val player = Player(session, guid, character.name, character.level)
        player.addComponent(MovementComponent())
        player.addBehavior(MovableBehavior)

        player.addComponent(CombatComponent())
        player.addBehavior(CombatBehavior)

        if (objectAbilityManager != null) {
            player.addBehavior(AbilityBehavior(objectAbilityManager))
        }

        // @todo change this
        player.addTrait(
            SpellCasterTrait(
                player,
                SpellBook(
                    hashMapOf(
                        1 to
                            SpellInfo(
                                1,
                                InstantLaunchInfo(
                                    ApplyEffectsSpellTrigger(arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(1, 5f))),
                                ),
                            ),
                    ),
                ),
            ),
        )
        player.registerAbilities(listOf(1, 2))

        return PlayerInitResult(player, character.position)
    }

    data class PlayerInitResult(
        val player: Player,
        val position: Position,
    )
}
