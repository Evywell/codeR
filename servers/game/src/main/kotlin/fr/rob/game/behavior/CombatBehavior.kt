package fr.rob.game.behavior

import fr.rob.game.combat.DamageSource
import fr.rob.game.component.CombatComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.message.DebugSignalMessage

object CombatBehavior : BehaviorInterface {
    private const val MAIN_HAND_WEAPON_DAMAGE_AMOUNT = 18

    override fun update(worldObject: WorldObject, deltaTime: Int) {
        val component = worldObject.getComponent<CombatComponent>() ?: return

        if (!component.isInCombatWithMainTarget()) {
            return
        }

        worldObject.controlledByGameSession?.send(
            DebugSignalMessage(
                "IS_IN_FRONT_OF",
                if (worldObject.isInFrontOf(component.mainTarget!!)) 1 else 0
            )
        )

        if (component.isWeaponSwingReady(deltaTime)) {
            performAttack(worldObject, component)

            return
        }
    }

    private fun performAttack(attacker: WorldObject, combatComponent: CombatComponent) {
        if (
            !attacker.isInMeleeRangeOf(combatComponent.mainTarget!!) ||
            !attacker.isInFrontOf(combatComponent.mainTarget!!)
        ) {
            return
        }

        val objectSheetBehavior = attacker.getBehavior<ObjectSheetBehavior>() ?: return

        objectSheetBehavior.applySingleDamage(
            combatComponent.mainTarget!!,
            DamageSource(attacker, MAIN_HAND_WEAPON_DAMAGE_AMOUNT),
            objectSheetBehavior.isCriticalHit(attacker)
        )

        combatComponent.weaponSwing()
    }

    override fun canApplyTo(worldObject: WorldObject): Boolean = worldObject.hasComponent(CombatComponent::class)
}