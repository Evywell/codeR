package fr.rob.game.behavior

import fr.rob.game.combat.DamageSource
import fr.rob.game.component.CombatComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.scheduler.TaskContext
import fr.rob.game.entity.scheduler.TaskGroup
import fr.rob.game.player.message.DebugSignalMessage

object CombatBehavior : BehaviorInterface {
    private const val MAIN_HAND_WEAPON_DAMAGE_AMOUNT = 18

    /**
     * Targets [target] and swings at the next update, then every [CombatComponent.MAIN_HAND_WEAPON_SPEED_MS].
     * Switching target while already swinging keeps the current cadence.
     */
    fun engage(attacker: WorldObject, target: WorldObject) {
        val component = attacker.getComponent<CombatComponent>() ?: return

        component.mainTargetGuid = target.guid

        if (!attacker.scheduler.isScheduled(TaskGroup.COMBAT_SWING)) {
            attacker.scheduler.schedule(0, TaskGroup.COMBAT_SWING) { context ->
                swing(attacker, context)
            }
        }
    }

    fun disengage(attacker: WorldObject) {
        attacker.getComponent<CombatComponent>()?.mainTargetGuid = null
        attacker.scheduler.cancelGroup(TaskGroup.COMBAT_SWING)
    }

    override fun update(worldObject: WorldObject, deltaTime: Int) {
        val component = worldObject.getComponent<CombatComponent>() ?: return
        val target = findTarget(worldObject, component) ?: return

        worldObject.controlledByGameSession?.send(
            DebugSignalMessage(
                "IS_IN_FRONT_OF",
                if (worldObject.isInFrontOf(target)) 1 else 0
            )
        )
    }

    private fun swing(attacker: WorldObject, context: TaskContext) {
        val component = attacker.getComponent<CombatComponent>() ?: return
        val target = findTarget(attacker, component) ?: return

        if (!attacker.isInMeleeRangeOf(target) || !attacker.isInFrontOf(target)) {
            // The weapon is ready: swing as soon as the target is reachable
            context.repeat(0)
            return
        }

        val objectSheetBehavior = attacker.getBehavior<ObjectSheetBehavior>()

        objectSheetBehavior?.applySingleDamage(
            target,
            DamageSource(attacker, MAIN_HAND_WEAPON_DAMAGE_AMOUNT),
            objectSheetBehavior.isCriticalHit(attacker)
        )

        context.repeat(CombatComponent.MAIN_HAND_WEAPON_SPEED_MS)
    }

    /**
     * Null, and combat stopped, when there is no target or it left the instance.
     */
    private fun findTarget(attacker: WorldObject, component: CombatComponent): WorldObject? {
        val targetGuid = component.mainTargetGuid ?: return null
        val target = attacker.mapInstance.findObjectByGuid(targetGuid)

        if (target == null) {
            disengage(attacker)
        }

        return target
    }

    override fun canApplyTo(worldObject: WorldObject): Boolean = worldObject.hasComponent(CombatComponent::class)
}
