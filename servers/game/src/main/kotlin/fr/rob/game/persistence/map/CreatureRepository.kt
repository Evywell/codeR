package fr.rob.game.persistence.map

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose
import fr.rob.game.entity.template.Creature
import java.sql.ResultSet

class CreatureRepository(private val db: Connection) : CreatureRepositoryInterface {

    override fun findByMapId(mapId: Int): List<Creature> {
        val stmt = db.createPreparedStatement(SEL_CREATURES_BY_MAP_ID)!!

        stmt.setInt(1, Creature.TYPE)
        stmt.setInt(2, mapId)
        db.execute(stmt)

        val rs = stmt.resultSet

        return returnAndClose(createCreatureCollectionFromResultSet(rs), rs, stmt)
    }

    private fun createCreatureCollectionFromResultSet(rs: ResultSet): List<Creature> {
        val creatures = ArrayList<Creature>()

        while (rs.next()) {
            creatures.add(createCreatureFromResultSetRow(rs))
        }

        return creatures
    }

    private fun createCreatureFromResultSetRow(rs: ResultSet): Creature =
        Creature(
            rs.getInt(1),
            rs.getFloat(2),
            rs.getFloat(3),
            rs.getFloat(4),
            rs.getFloat(5),
            rs.getString(6)
        )

    companion object {
        const val SEL_CREATURES_BY_MAP_ID =
            "SELECT o.map_id, o.position_x, o.position_y, o.position_z, o.orientation, t.name " +
                    "FROM objects o " +
                    "INNER JOIN creatures c ON (c.object_id = o.id AND o.type = ?) " +
                    "INNER JOIN creature_templates t ON t.id = o.template_id " +
                    "WHERE o.map_id = ?"
    }
}
