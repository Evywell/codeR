package fr.rob.shared.instances

import fr.rob.core.database.Connection
import fr.rob.core.database.getIntOrNull
import fr.rob.core.database.returnAndClose

class InstancesRepository(private val db: Connection) : InstancesRepositoryInterface {

    override fun insert(mapId: Int, zoneId: Int?, type: Int, orchestratorId: Int): Instance {
        val stmt = db.createPreparedStatement(INS_INSTANCE, true)!!

        stmt.setInt(1, mapId)
        stmt.setInt(2, orchestratorId)
        stmt.setInt(3, type)

        db.executeUpdate(stmt)

        val keys = stmt.generatedKeys

        keys.next()

        val instanceId = keys.getInt(1)

        // Zone
        if (zoneId != null) {
            insertZone(instanceId, zoneId)
        }

        return returnAndClose(Instance(instanceId, mapId, zoneId), keys, stmt)
    }

    override fun insertZone(instanceId: Int, zoneId: Int) {
        val stmt = db.createPreparedStatement(INS_INSTANCE_ZONE)!!

        stmt.setInt(1, instanceId)
        stmt.setInt(2, zoneId)

        db.executeInsertOrThrow(stmt, "Cannot insert zone instance with tuple ($instanceId, $zoneId)")
        stmt.close()
    }

    override fun getGlobalInstancesByOrchestratorAndType(orchestratorId: Int, type: Int): List<Instance> {
        val stmt = db.createPreparedStatement(SEL_GLOBAL_INSTANCES_BY_ORCHESTRATOR_ID_AND_TYPE)!!

        stmt.setInt(1, orchestratorId)
        stmt.setInt(2, type)

        db.execute(stmt)

        val rs = stmt.resultSet
        val instances = ArrayList<Instance>()

        while (rs.next()) {
            instances.add(Instance(rs.getInt(1), rs.getInt(2), getIntOrNull(rs, 3)))
        }

        return returnAndClose(instances, rs, stmt)
    }

    companion object {
        const val SEL_GLOBAL_INSTANCES_BY_ORCHESTRATOR_ID_AND_TYPE =
            "SELECT i.id, i.map_id, iz.zone_id " +
                    "FROM instances i " +
                    "LEFT JOIN instance_zones iz ON iz.instance_id = i.id " +
                    "WHERE i.orchestrator_owner_id = ? AND i.type = ?;"

        const val INS_INSTANCE = "INSERT INTO instances (map_id, orchestrator_owner_id, type) VALUES (?, ?, ?);"
        const val INS_INSTANCE_ZONE = "INSERT INTO instance_zone (instance_id, zone_id) VALUES (?, ?);"
    }
}
