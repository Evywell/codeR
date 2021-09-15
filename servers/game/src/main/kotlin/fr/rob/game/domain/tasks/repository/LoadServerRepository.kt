package fr.rob.game.domain.tasks.repository

import fr.rob.core.database.Connection
import fr.rob.core.database.closeCursor
import fr.rob.core.database.returnAndCloseWithCallback
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.Zone
import org.codehaus.jackson.map.ObjectMapper
import java.lang.RuntimeException

class LoadServerRepository(private val connection: Connection) : LoadServerRepositoryInterface {

    override fun getServerInfo(server: Server): ServerInfo {
        val serverInfoStatement = connection.createPreparedStatement(STMT_CONFIG_SEL_SERVER_INSTANCES)!!

        serverInfoStatement.setString(1, server.serverName)
        connection.execute(serverInfoStatement)

        val om = ObjectMapper()
        val zones = ArrayList<Zone>()
        val rs = serverInfoStatement.resultSet

        if (!rs.next()) {
            closeCursor(rs, serverInfoStatement)

            throw RuntimeException("Cannot retrieve server information: ${server.serverName}")
        }

        val serverName = rs.getString(1)
        val serverAddress = rs.getString(2)

        do {
            val zone: Zone = om.readValue(rs.getString(4), Zone::class.java)

            zone.mapId = rs.getInt(3)
            zones.add(zone)
        } while (rs.next())

        return returnAndCloseWithCallback(rs, serverInfoStatement) {
            ServerInfo(serverName, serverAddress, zones)
        }
    }

    companion object {
        const val STMT_CONFIG_SEL_SERVER_INSTANCES =
            "SELECT s.name, s.address, z.map_id, z.positions " +
                    "FROM servers s " +
                    "JOIN servers_zones si ON si.server_id = s.id " +
                    "JOIN zones z ON z.id = si.zone_id " +
                    "WHERE s.name = ?;"
    }
}
