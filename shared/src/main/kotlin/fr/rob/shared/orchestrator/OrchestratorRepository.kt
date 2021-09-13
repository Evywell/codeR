package fr.rob.shared.orchestrator

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose
import fr.rob.core.database.returnAndCloseWithCallback

class OrchestratorRepository(private val db: Connection) : OrchestratorRepositoryInterface {

    override fun getOrchestratorById(id: Int): Orchestrator? {
        val stmt = db.createPreparedStatement(SEL_BY_ID)!!
        stmt.setInt(1, id)

        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            return returnAndClose(null, rs, stmt)
        }

        return returnAndCloseWithCallback(rs, stmt) {
            Orchestrator(rs.getInt(1), rs.getString(2), rs.getString(3))
        }
    }

    companion object {
        const val SEL_BY_ID = "SELECT id, address, token FROM orchestrators WHERE id = ?;"
    }
}
