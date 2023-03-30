package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.behavior.BehaviorInterface
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.notifier.WorldObjectVisitorInterface
import fr.rob.game.domain.event.DomainEventCarrierInterface
import fr.rob.game.domain.event.DomainEventContainer
import fr.rob.game.domain.event.DomainEventInterface
import fr.rob.game.domain.instance.MapInstance
import fr.rob.game.domain.player.session.GameSession
import fr.rob.game.domain.terrain.grid.Cell

open class WorldObject(
    val guid: ObjectGuid
) : DomainEventCarrierInterface {
    var isInWorld = false
    var cell: Cell? = null
    lateinit var mapInstance: MapInstance
    lateinit var position: Position

    var controlledByGameSession: GameSession? = null

    protected val behaviors = ArrayList<BehaviorInterface>()

    private val domainEventContainer = DomainEventContainer()

    open fun update(deltaTime: Int) {
        domainEventContainer.resetContainer()
        behaviors.forEach { it.update(deltaTime) }
    }

    open fun accept(worldObjectVisitor: WorldObjectVisitorInterface) {
        // @todo remove ?
        worldObjectVisitor.visit(this)
    }

    override fun pushEvent(event: DomainEventInterface) {
        domainEventContainer.pushEvent(event)
    }

    override fun getDomainEventContainer(): Collection<DomainEventInterface> =
        domainEventContainer.getDomainEventContainer()
}
