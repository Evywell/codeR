package fr.rob.game.infrastructure.config.database

const val STMT_CONFIG_SEL_SERVER_INSTANCES =
    "SELECT s.name, s.address, z.map_id, z.positions " +
            "FROM servers s " +
            "JOIN servers_zones si ON si.server_id = s.id " +
            "JOIN zones z ON z.id = si.zone_id " +
            "WHERE s.name = ?;"
