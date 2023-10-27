package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.movement.WorldObjectMovedEvent
import fr.rob.game.domain.entity.notifier.WorldObjectVisitorInterface
import fr.rob.game.domain.event.DomainEventCarrierInterface
import fr.rob.game.domain.event.DomainEventContainer
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.grid.Cell
import java.util.Optional
import kotlin.reflect.KClass

open class WorldObject(
    val guid: ObjectGuid
) : DomainEventCarrierInterface {
    var isInWorld = false
    var cell: Cell? = null
    lateinit var mapInstance: MapInstance
    lateinit var position: Position

    var controlledByGameSession: GameSession? = null

    private val domainEventContainer = DomainEventContainer()
    private val traits = HashMap<KClass<*>, Any>()

    open fun update(deltaTime: Int) {
        domainEventContainer.resetContainer()

        traits.forEach { (_, trait) ->
            if (trait is UpdatableTraitInterface) {
                trait.update(deltaTime)
            }
        }
    }

    open fun accept(worldObjectVisitor: WorldObjectVisitorInterface) {
        // @todo remove ?
        worldObjectVisitor.visit(this)
    }

    fun setPosition(x: Float, y: Float, z: Float, orientation: Float) {
        position.x = x
        position.y = y
        position.z = z
        position.orientation = orientation

        pushEvent(WorldObjectMovedEvent(this))
    }

    fun addIntoInstance(instance: MapInstance, toPosition: Position) {
        mapInstance = instance
        position = toPosition
        isInWorld = true

        val addedIntoCell = mapInstance.grid.addWorldObject(this)

        cell = addedIntoCell
    }

    fun scheduleRemoveFromInstance() {
        mapInstance.scheduleRemoveFromInstance(this)
        cell = null
    }

    fun addTrait(trait: Any) {
        traits[trait::class] = trait
    }

    fun <T : Any> getTrait(traitType: KClass<T>): Optional<T> = Optional.ofNullable(traits[traitType] as T?)

    inline fun <reified T : Any> getTrait(): Optional<T> {
        return getTrait(T::class)
    }

    override fun pushEvent(event: DomainEventInterface) {
        domainEventContainer.pushEvent(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> =
        domainEventContainer.getDomainEventContainer()
}
