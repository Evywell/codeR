package fr.rob.game.domain.setup.tasks

import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.database.Connection
import fr.rob.core.database.ConnectionManager
import fr.rob.core.initiator.TaskInterface
import fr.rob.game.CONFIG_DEFAULT
import fr.rob.game.DATABASE
import fr.rob.game.DB_CONFIG
import fr.rob.game.SERVER
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.Zone
import fr.rob.game.domain.setup.Setup
import fr.rob.game.infrastructure.config.database.STMT_CONFIG_SEL_SERVER_INSTANCES
import org.codehaus.jackson.map.ObjectMapper

class TaskLoadServerConfig(private val app: BaseApplication, private val setup: Setup) : TaskInterface {

    override fun run() {
        val config: Config = app.getConfig(CONFIG_DEFAULT)

        val connectionManager: ConnectionManager = config
            .get(DATABASE) as ConnectionManager

        // Init config
        val connection: Connection = connectionManager.getConnection(DB_CONFIG)
            ?: throw Exception("Cannot get connection $DB_CONFIG")

        @Suppress("UNCHECKED_CAST") // Cannot do otherwise
        val servers: Array<Server> = config
            .get(SERVER) as Array<Server>

        val om = ObjectMapper()

        val stmt = connection.getPreparedStatement(STMT_CONFIG_SEL_SERVER_INSTANCES)

        for (server in servers) {
            stmt.setString(1, server.serverName)
            stmt.execute()

            val rs = stmt.resultSet

            while (rs.next()) {
                val zone: Zone = om.readValue(rs.getString(4), Zone::class.java)
                zone.mapId = rs.getInt(3)
                server.zones.add(zone)
            }

            if (server.zones.isEmpty()) {
                throw RuntimeException("Cannot find server name ${server.serverName} maps")
            }

            // The serverAddress are the same in each iteration, so we use the last one
            server.serverAddress = rs.getString(2)
        }

        setup.setServers(servers)
    }
}
