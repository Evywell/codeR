package fr.rob.orchestrator.instances

import fr.rob.core.database.Connection
import fr.rob.core.database.getIntOrNull
import fr.rob.core.database.returnAndClose
import fr.rob.orchestrator.nodes.GameNode

class DefaultInstancesRepository(private val db: Connection) : DefaultInstancesRepositoryInterface {

    override fun getDefaultInstancesByNode(node: GameNode): List<DefaultInstance> {
        val stmt = db.createPreparedStatement(SEL_DEFAULT_INSTANCES_BY_NODE)!!

        stmt.setString(1, node.name)
        db.execute(stmt)

        val rs = stmt.resultSet

        val defaultInstances = ArrayList<DefaultInstance>()

        while (rs.next()) {
            defaultInstances.add(DefaultInstance(rs.getInt(1), getIntOrNull(rs, 2), rs.getString(3), rs.getInt(4)))
        }

        return returnAndClose(defaultInstances, rs, stmt)
    }

    companion object {
        const val SEL_DEFAULT_INSTANCES_BY_NODE =
            "SELECT map_id, zone_id, node_name, type FROM default_instances WHERE node_name = ?;"
    }
}
