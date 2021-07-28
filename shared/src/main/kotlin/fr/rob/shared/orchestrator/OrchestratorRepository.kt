package fr.rob.shared.orchestrator

import fr.rob.core.database.Connection
import fr.rob.core.database.returnAndClose

class OrchestratorRepository(private val db: Connection) : OrchestratorRepositoryInterface {

    override fun getOrchestratorById(id: Int): Orchestrator? {
        val stmt = db.getPreparedStatement(SEL_BY_ID)
        stmt.setInt(1, id)

        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            rs.close()

            return null
        }

        return returnAndClose(Orchestrator(rs.getInt(1), rs.getString(2), rs.getString(3)), rs)
    }

    companion object {
        const val SEL_BY_ID = "SELECT id, address, token FROM orchestrators WHERE id = ?;"
    }
}