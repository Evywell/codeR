package fr.rob.game.script

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.entity.ScriptedWorldObject
import fr.rob.game.entity.WorldObject
import fr.rob.game.spell.Spell
import fr.rob.game.spell.SpellBook
import fr.rob.game.spell.SpellInfo
import fr.rob.game.spell.effect.InstantAoeDamageEffect
import fr.rob.game.spell.target.SpellTargetParameter
import fr.rob.game.spell.trigger.ApplyEffectsSpellTrigger
import fr.rob.game.spell.type.instant.InstantLaunchInfo

class InstantDamageAoeScript(
    private val owner: ScriptedWorldObject,
    private val caster: WorldObject,
    damageValue: Int,
    radius: Float,
    frequencyMs: Int,
    durationMs: Int,
) : ScriptInterface {
    private val spellBook = SpellBook(
        hashMapOf(
            1 to SpellInfo(
                1,
                InstantLaunchInfo(ApplyEffectsSpellTrigger(arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(damageValue, radius))))
            ),
        ),
    )

    private val frequencyTimer = IntervalTimer(frequencyMs)
    private val durationTimer = IntervalTimer(durationMs)

    override fun update(deltaTime: Int) {
        frequencyTimer.update(deltaTime)
        durationTimer.update(deltaTime)

        if (durationTimer.passed()) {
            owner.scheduleRemoveFromInstance()
            return
        }

        if (!frequencyTimer.passed()) {
            return
        }

        frequencyTimer.reset()

        val spell = Spell(
            spellBook.getSpellInfo(1).get(),
            owner,
            SpellTargetParameter(null, owner.mapInstance),
        )

        spell.cast()
    }
}
