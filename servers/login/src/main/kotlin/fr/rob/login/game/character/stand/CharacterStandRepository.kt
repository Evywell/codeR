package fr.rob.login.game.character.stand

import fr.rob.core.database.Connection
import fr.rob.core.database.getIntAndClose
import fr.rob.core.database.returnAndClose

class CharacterStandRepository(private val db: Connection) : CharacterStandRepositoryInterface {

    override fun getCurrentCharacterId(accountId: Int): Int {
        val stmt = db.createPreparedStatement(SEL_LAST_SELECTED_CHARACTER_BY_USER_ID)!!

        stmt.setInt(1, accountId)
        stmt.execute()

        val rs = stmt.resultSet

        return if (rs.next())
            getIntAndClose(1, rs, stmt)
        else
            returnAndClose(0, rs, stmt)
    }

    companion object {
        const val SEL_LAST_SELECTED_CHARACTER_BY_USER_ID =
            "SELECT id FROM characters WHERE account_id = ? ORDER BY last_selected_at DESC LIMIT 1"
    }
}
