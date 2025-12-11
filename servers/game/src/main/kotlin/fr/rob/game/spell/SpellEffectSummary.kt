package fr.rob.game.spell

import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject

class SpellEffectSummary : Map<WorldObject, ArrayList<DamageSource>> {
    private val targetDamageSources = HashMap<WorldObject, ArrayList<DamageSource>>()

    fun addDamageSourceForTarget(target: WorldObject, damageSource: DamageSource) {
        if (!targetDamageSources.containsKey(target)) {
            targetDamageSources[target] = ArrayList()
        }

        targetDamageSources[target]!!.add(damageSource)
    }

    override val entries: Set<Map.Entry<WorldObject, ArrayList<DamageSource>>>
        get() = targetDamageSources.entries
    override val keys: Set<WorldObject>
        get() = targetDamageSources.keys
    override val size: Int
        get() = targetDamageSources.size
    override val values: Collection<ArrayList<DamageSource>>
        get() = targetDamageSources.values

    override fun isEmpty(): Boolean = targetDamageSources.isEmpty()

    override fun get(key: WorldObject): ArrayList<DamageSource>? = targetDamageSources[key]

    override fun containsValue(value: ArrayList<DamageSource>): Boolean = targetDamageSources.containsValue(value)

    override fun containsKey(key: WorldObject): Boolean = targetDamageSources.containsKey(key)
}
