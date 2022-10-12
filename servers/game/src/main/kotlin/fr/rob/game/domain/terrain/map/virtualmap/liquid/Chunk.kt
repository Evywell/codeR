package fr.rob.game.domain.terrain.map.virtualmap.liquid

import java.util.Optional

class Chunk(private val areas: Array<Area>) {
    fun getLiquidAreaByHeight(z: Float): Optional<Area> {
        for (area in areas) {
            if (area.isInsideForHeight(z)) {
                return Optional.of(area)
            }
        }

        return Optional.empty()
    }
}
