package fr.rob.orchestrator.api.instance

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose

class InstancesRepository(private val db: Connection) : InstancesRepositoryInterface {
    override fun insert(mapId: Int, zoneId: Int, type: Int): Instance {
        val stmt = db.createPreparedStatement(INS_INSTANCE, true)!!

        stmt.setInt(1, mapId)
        stmt.setInt(2, zoneId)
        stmt.setInt(3, type)

        db.executeUpdate(stmt)

        val keys = stmt.generatedKeys

        keys.next()

        val instanceId = keys.getInt(1)

        return returnAndClose(Instance(instanceId, mapId, zoneId, type), keys, stmt)
    }

    override fun getGlobalInstancesByOrchestratorAndType(orchestratorId: Int, type: Int): List<Instance> {
        val stmt = db.createPreparedStatement(SEL_GLOBAL_INSTANCES)!!

        stmt.setInt(1, type)

        db.execute(stmt)

        val rs = stmt.resultSet
        val instances = ArrayList<Instance>()

        while (rs.next()) {
            instances.add(Instance(rs.getInt(1), rs.getInt(2), rs.getInt(3), Instance.TYPE_GLOBAL))
        }

        return returnAndClose(instances, rs, stmt)
    }

    companion object {
        const val SEL_GLOBAL_INSTANCES = "SELECT i.id, i.map_id, i.zone_id FROM instances i WHERE i.type = ?;"
        const val INS_INSTANCE = "INSERT INTO instances (map_id, zone_id, type) VALUES (?, ?, ?);"
    }
}
