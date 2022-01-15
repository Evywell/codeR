package fr.rob.game.game.world.map.loader

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose
import fr.rob.game.game.world.map.MapInfo
import fr.rob.game.game.world.map.ZoneInfo

class MapRepository(private val db: Connection) : MapRepositoryInterface {

    override fun getMapInfo(mapId: Int, zoneId: Int?): Pair<MapInfo, ZoneInfo> {
        val withZone = zoneId != null

        val fields = StringBuilder("m.name, m.width, m.height")

        if (withZone) {
            fields.append(" z.name, z.width, z.height, z.offset_x, z.offset_y")
        }

        val queryWithZone =
            "SELECT $fields FROM maps m LEFT JOIN zones z ON z.map_id = m.id AND z.id = ? WHERE m.id = ?"
        val queryWithoutZone = "SELECT $fields FROM maps m WHERE m.id = ?"

        val query = if (withZone) queryWithZone else queryWithoutZone

        val stmt = db.createPreparedStatement(query)!!

        if (withZone) {
            stmt.setInt(1, zoneId!!)
            stmt.setInt(2, mapId)
        } else {
            stmt.setInt(1, mapId)
        }

        db.execute(stmt)

        val rs = stmt.resultSet

        val mapInfo = MapInfo(rs.getString(1), rs.getInt(2), rs.getInt(3))
        val zoneInfo = ZoneInfo(
            rs.getString(4),
            rs.getInt(5),
            rs.getInt(6),
            rs.getFloat(7),
            rs.getFloat(8)
        )

        return returnAndClose(Pair(mapInfo, zoneInfo), rs, stmt)
    }
}
