package fr.rob.game.entity.visibility

import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.scheduler.TaskGroup
import fr.rob.game.instance.MapInstance
import fr.rob.game.player.Player
import fr.rob.game.player.message.NearbyObjectMessage
import fr.rob.game.player.message.ObjectDescriptionMessage
import fr.rob.game.player.message.ObjectDestroyMessage

/**
 * Keeps, for each player, the set of objects its client knows, and sends create / destroy messages when it changes.
 *
 * An object becomes visible inside [ENTER_RADIUS] and stops being visible beyond [LEAVE_RADIUS], so an object moving
 * on the edge doesn't flicker.
 */
class VisibilityService {
    /**
     * Refreshes the visibility of every player of the instance that changed cell or whose refresh interval elapsed.
     */
    fun update(instance: MapInstance) {
        val grid = instance.grid

        grid.getObjectsByType(ObjectGuid.GUID_TYPE.PLAYER).forEach { worldObject ->
            val player = worldObject as Player

            if (!player.isInWorld) {
                return@forEach
            }

            val visibility = player.getComponent<VisibilityComponent>() ?: VisibilityComponent().also { player.addComponent(it) }
            val cell = grid.getRegisteredCell(player)

            if (visibility.initialized && cell == visibility.lastCell && !visibility.refreshDue) {
                return@forEach
            }

            if (!visibility.initialized) {
                player.controlledByGameSession?.send(NearbyObjectMessage(player.guid, player.position))
                visibility.initialized = true
                schedulePeriodicRefresh(player, visibility)
            }

            refresh(player, visibility)

            visibility.lastCell = cell
            visibility.refreshDue = false
        }
    }

    /**
     * Players whose client knows the object.
     */
    fun observersOf(worldObject: WorldObject): List<Player> =
        worldObject.mapInstance.grid.findObjectsInsideRadius(worldObject.position, LEAVE_RADIUS)
            .filterIsInstance<Player>()
            .filter { it !== worldObject && it.getComponent<VisibilityComponent>()?.sees(worldObject.guid) == true }

    // Catches objects moving around a player that doesn't move itself
    private fun schedulePeriodicRefresh(player: Player, visibility: VisibilityComponent) {
        player.scheduler.schedule(REFRESH_INTERVAL_MS, TaskGroup.VISIBILITY_REFRESH) { context ->
            visibility.refreshDue = true
            context.repeat()
        }
    }

    private fun refresh(player: Player, visibility: VisibilityComponent) {
        val candidates = player.mapInstance.grid.findObjectsInsideRadius(player.position, LEAVE_RADIUS)
        val nowVisible = HashMap<ObjectGuid, WorldObject>()

        for (candidate in candidates) {
            if (candidate === player) {
                continue
            }

            val alreadyVisible = visibility.sees(candidate.guid)

            if (alreadyVisible || squaredPlanarDistance(player, candidate) <= ENTER_RADIUS * ENTER_RADIUS) {
                nowVisible[candidate.guid] = candidate
            }
        }

        val session = player.controlledByGameSession

        nowVisible.forEach { (guid, worldObject) ->
            if (!visibility.sees(guid)) {
                session?.send(NearbyObjectMessage(guid, worldObject.position))
                session?.send(ObjectDescriptionMessage(worldObject))
            }
        }

        visibility.visibleGuids.filterNot { nowVisible.containsKey(it) }.forEach { guid ->
            session?.send(ObjectDestroyMessage(guid))
        }

        visibility.visibleGuids.clear()
        visibility.visibleGuids.addAll(nowVisible.keys)
    }

    private fun squaredPlanarDistance(a: WorldObject, b: WorldObject): Float {
        val dx = a.position.x - b.position.x
        val dy = a.position.y - b.position.y

        return dx * dx + dy * dy
    }

    companion object {
        const val ENTER_RADIUS = 50f
        const val LEAVE_RADIUS = 60f
        const val REFRESH_INTERVAL_MS = 200
    }
}
