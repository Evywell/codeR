package fr.rob.game.infra.mysql.character

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose
import fr.rob.game.domain.character.CheckCharacterExistInterface

class MysqlCheckCharacterExist(private val db: Connection) : CheckCharacterExistInterface {
    override fun characterExistsForAccount(characterId: Int, accountId: Int): Boolean {
        val stmt = db.createPreparedStatement("SELECT EXISTS(SELECT id FROM characters WHERE id = ? AND account_id = ?)")
            ?: return false

        stmt.setInt(1, characterId)
        stmt.setInt(2, accountId)

        db.execute(stmt)

        val rs = stmt.resultSet

        return returnAndClose(rs.next(), rs, stmt)
    }
}
