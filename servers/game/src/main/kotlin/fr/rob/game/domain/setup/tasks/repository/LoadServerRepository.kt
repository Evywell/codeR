package fr.rob.game.domain.setup.tasks.repository

import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.Zone
import fr.rob.game.infrastructure.database.Connection
import fr.rob.game.infrastructure.database.PreparedStatement
import org.codehaus.jackson.map.ObjectMapper

class LoadServerRepository(connection: Connection) : LoadServerRepositoryInterface {

    private val serverInfoStatement: PreparedStatement =
        connection.getPreparedStatement(STMT_CONFIG_SEL_SERVER_INSTANCES)

    override fun getServerInfo(server: Server): ServerInfo {
        serverInfoStatement.setString(1, server.serverName)
        serverInfoStatement.execute()

        val om = ObjectMapper()
        val zones = ArrayList<Zone>()
        val rs = serverInfoStatement.resultSet

        while (rs.next()) {
            val zone: Zone = om.readValue(rs.getString(4), Zone::class.java)

            zone.mapId = rs.getInt(3)
            zones.add(zone)
        }

        return ServerInfo(rs.getString(1), rs.getString(2), zones)
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
