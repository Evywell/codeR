package fr.rob.game.entity.visibility

import fr.rob.core.misc.clock.IntervalTimer
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.map.grid.Cell

/**
 * Objects the client of a player currently knows about. Owned and updated by [VisibilityService].
 */
class VisibilityComponent {
    internal val visibleGuids = HashSet<ObjectGuid>()
    internal val refreshTimer = IntervalTimer(VisibilityService.REFRESH_INTERVAL_MS)
    internal var lastCell: Cell? = null
    internal var initialized = false

    fun sees(guid: ObjectGuid): Boolean = visibleGuids.contains(guid)
}
