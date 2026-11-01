package fr.rob.game.behavior

import fr.rob.game.combat.DamageSource
import fr.rob.game.component.CombatComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.player.message.DebugSignalMessage

object CombatBehavior : BehaviorInterface {
    private const val MAIN_HAND_WEAPON_DAMAGE_AMOUNT = 18

    override fun update(worldObject: WorldObject, deltaTime: Int) {
        val component = worldObject.getComponent<CombatComponent>() ?: return
        val targetGuid = component.mainTargetGuid ?: return
        val target = worldObject.mapInstance.findObjectByGuid(targetGuid)

        if (target == null) {
            component.disengage()
            return
        }

        worldObject.controlledByGameSession?.send(
            DebugSignalMessage(
                "IS_IN_FRONT_OF",
                if (worldObject.isInFrontOf(target)) 1 else 0
            )
        )

        if (component.isWeaponSwingReady(deltaTime)) {
            performAttack(worldObject, target, component)

            return
        }
    }

    private fun performAttack(attacker: WorldObject, target: WorldObject, combatComponent: CombatComponent) {
        if (!attacker.isInMeleeRangeOf(target) || !attacker.isInFrontOf(target)) {
            return
        }

        val objectSheetBehavior = attacker.getBehavior<ObjectSheetBehavior>() ?: return

        objectSheetBehavior.applySingleDamage(
            target,
            DamageSource(attacker, MAIN_HAND_WEAPON_DAMAGE_AMOUNT),
            objectSheetBehavior.isCriticalHit(attacker)
        )

        combatComponent.weaponSwing()
    }

    override fun canApplyTo(worldObject: WorldObject): Boolean = worldObject.hasComponent(CombatComponent::class)
}