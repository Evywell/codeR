package fr.rob.orchestrator.api.instance

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose

class DefaultInstancesRepository(private val db: Connection) : DefaultInstancesRepositoryInterface {

    override fun getDefaultInstancesByNode(nodeName: String): List<DefaultInstance> {
        val stmt = db.createPreparedStatement(SEL_DEFAULT_INSTANCES_BY_NODE)!!

        stmt.setString(1, nodeName)
        db.execute(stmt)

        val rs = stmt.resultSet

        val defaultInstances = ArrayList<DefaultInstance>()

        while (rs.next()) {
            defaultInstances.add(DefaultInstance(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4)))
        }

        return returnAndClose(defaultInstances, rs, stmt)
    }

    companion object {
        const val SEL_DEFAULT_INSTANCES_BY_NODE =
            "SELECT map_id, zone_id, node_name, type FROM default_instances WHERE node_name = ?;"
    }
}
