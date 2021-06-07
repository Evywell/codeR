package fr.rob.login.game.character.stand

import fr.rob.core.database.Connection
import fr.rob.core.database.getIntAndClose

class CharacterStandRepository(private val db: Connection) : CharacterStandRepositoryInterface {

    override fun getCurrentCharacterId(userId: Int): Int {
        val stmt = db.getPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)

        stmt.setInt(1, userId)
        stmt.execute()

        val rs = stmt.resultSet

        if (rs.next()) {
            return getIntAndClose(1, rs)
        }

        return 0
    }

    companion object {
        const val SEL_LAST_SELECTED_CHARACTER_BY_USER_ID =
            "SELECT id FROM characters WHERE account_id = ? ORDER BY last_selected_at DESC LIMIT 1"
    }
}
