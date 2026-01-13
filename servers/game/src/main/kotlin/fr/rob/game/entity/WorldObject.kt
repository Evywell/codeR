package fr.rob.game.entity

import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.movement.WorldObjectMovedEvent
import fr.rob.game.entity.notifier.WorldObjectVisitorInterface
import fr.rob.game.event.DomainEventCarrierInterface
import fr.rob.game.event.DomainEventContainer
import fr.rob.game.event.DomainEventInterface
import fr.rob.game.instance.MapInstance
import fr.rob.game.player.session.GameSession
import fr.rob.game.map.grid.Cell
import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.behavior.BehaviorInterface
import fr.rob.game.behavior.BehaviorSet
import java.util.Optional
import kotlin.reflect.KClass

open class WorldObject(
    val guid: ObjectGuid,
) : DomainEventCarrierInterface {
    var isInWorld = false
    lateinit var mapInstance: MapInstance
    lateinit var position: Position

    var controlledByGameSession: GameSession? = null

    private val knownAbilities = ArrayList<Int>()
    private val ongoingAbilities = ArrayList<Ability>()
    private val domainEventContainer = DomainEventContainer()
    private val traits = HashMap<KClass<*>, Any>()
    private val components = mutableMapOf<KClass<*>, Any>()
    private val behaviors = BehaviorSet()

    open fun onUpdate(deltaTime: Int) {
        traits.forEach { (_, trait) ->
            if (trait is UpdatableTraitInterface) {
                trait.update(deltaTime)
            }
        }

        behaviors.update(this, deltaTime)
    }

    open fun onAfterUpdate() {
        domainEventContainer.resetContainer()
    }

    open fun accept(worldObjectVisitor: WorldObjectVisitorInterface) {
        // @todo remove ?
        worldObjectVisitor.visit(this)
    }

    fun isInMeleeRangeOf(target: WorldObject): Boolean =
        position.getSquaredDistanceWith(target.position) <=
            DEFAULT_MELEE_RANGE_METERS * DEFAULT_MELEE_RANGE_METERS * DEFAULT_MELEE_RANGE_METERS

    fun isInFrontOf(target: WorldObject): Boolean = position.hasInArc(Position.ANGLE_2_PI_3, target.position)

    fun isBehindOf(target: WorldObject): Boolean = !target.isInFrontOf(this)

    fun setPosition(
        x: Float,
        y: Float,
        z: Float,
        orientation: Float,
    ) {
        position.x = x
        position.y = y
        position.z = z
        position.orientation = orientation

        pushEvent(WorldObjectMovedEvent(this))
    }

    fun getCell(): Cell {
        return mapInstance.getWorldObjectCell(this)
    }

    fun addIntoInstance(instance: MapInstance, toPosition: Position) {
        mapInstance = instance
        position = toPosition
        isInWorld = true

        mapInstance.addInInstance(this)
    }

    fun scheduleRemoveFromInstance() {
        mapInstance.scheduleRemoveFromInstance(this)
        isInWorld = false
    }

    @Deprecated("Use ECS instead")
    fun addTrait(trait: Any) {
        traits[trait::class] = trait
    }

    @Deprecated("Use ECS instead")
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getTrait(traitType: KClass<T>): Optional<T> = Optional.ofNullable(traits[traitType] as T?)

    @Deprecated("Use ECS instead")
    inline fun <reified T : Any> getTrait(): Optional<T> = getTrait(T::class)

    fun registerAbilities(knownAbilities: List<Int>) {
        this.knownAbilities.addAll(knownAbilities)
    }

    fun hasAbility(abilityInfo: AbilityInfo): Boolean = knownAbilities.contains(abilityInfo.identifier)

    fun addOngoingAbility(ability: Ability) {
        ongoingAbilities.add(ability)
    }

    fun getOngoingAbilities(): MutableList<Ability> = ongoingAbilities

    fun addBehavior(behavior: BehaviorInterface) {
        behaviors.addBehavior(behavior)
    }

    fun <T : BehaviorInterface> getBehavior(type: KClass<T>): T? = behaviors.get(type)

    inline fun <reified T : BehaviorInterface> getBehavior(): T? {
        return getBehavior(T::class)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getComponent(type: KClass<T>): T? {
        return components[type] as? T
    }

    inline fun <reified T : Any> getComponent(): T? {
        return getComponent(T::class)
    }

    fun hasComponent(type: KClass<*>): Boolean = components.containsKey(type)

    fun getComponentTypes(): Set<KClass<*>> = components.keys

    fun addComponent(component: Any) {
        components[component::class] = component

        if (this::mapInstance.isInitialized) {
            mapInstance.grid.updateWorldObject(this)
        }
    }

    override fun pushEvent(event: DomainEventInterface) {
        domainEventContainer.pushEvent(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> = domainEventContainer.getDomainEventContainer()

    companion object {
        const val DEFAULT_MELEE_RANGE_METERS = 2
    }
}
