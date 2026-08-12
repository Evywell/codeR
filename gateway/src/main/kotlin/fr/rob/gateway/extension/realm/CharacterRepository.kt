package fr.rob.gateway.extension.realm

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose

class CharacterRepository(private val connection: Connection) {
    fun exists(characterId: Int): Boolean {
        val stmt = connection.createPreparedStatement("SELECT 1 FROM characters WHERE id = ?") ?: return false
        stmt.setInt(1, characterId)

        connection.execute(stmt)

        val rs = stmt.resultSet

        return returnAndClose(rs.next(), rs, stmt)
    }
}