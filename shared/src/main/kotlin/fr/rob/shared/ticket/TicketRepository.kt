package fr.rob.shared.ticket

import fr.rob.core.database.Connection
import fr.rob.core.database.dateToTimestamp
import fr.rob.core.database.exception.InsertException
import fr.rob.core.database.getIntOrNull
import fr.rob.core.database.returnAndClose
import java.sql.ResultSet
import java.sql.Types
import java.util.Date

class TicketRepository(private val playersDb: Connection) : TicketRepositoryInterface {

    override fun byToken(token: String): Ticket? {
        val stmt = playersDb.createPreparedStatement(SEL_TICKET_BY_TOKEN)!!

        stmt.setString(1, token)
        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            return returnAndClose(null, rs, stmt)
        }

        return returnAndClose(ticketFromResultSet(rs), rs, stmt)
    }

    override fun byAccountId(accountId: Int): Ticket? {
        val stmt = playersDb.createPreparedStatement(SEL_TICKET_BY_ACCOUNT_ID)!!

        stmt.setInt(1, accountId)
        stmt.execute()

        val rs = stmt.resultSet

        if (!rs.next()) {
            return returnAndClose(null, rs, stmt)
        }

        return returnAndClose(ticketFromResultSet(rs), rs, stmt)
    }

    override fun removeByAccountId(accountId: Int) {
        val stmt = playersDb.createPreparedStatement(DEL_TICKET_BY_ACCOUNT_ID)!!

        stmt.setInt(1, accountId)
        stmt.execute()
        stmt.close()
    }

    override fun insert(
        token: String,
        accountId: Int,
        characterId: Int,
        sourceId: Int?,
        targetId: Int,
        expireAt: Date
    ): Ticket {
        val stmt = playersDb.createPreparedStatement(INS_TICKET)!!

        stmt.setString(1, token)
        stmt.setInt(2, accountId)
        stmt.setInt(3, characterId)
        stmt.setInt(5, targetId)
        stmt.setBoolean(6, false)
        stmt.setTimestamp(7, dateToTimestamp(expireAt))

        if (sourceId == null) {
            stmt.setNull(4, Types.INTEGER)
        } else {
            stmt.setInt(4, sourceId)
        }

        if (!stmt.execute()) {
            stmt.close()

            throw InsertException("Cannot insert ticket")
        }

        stmt.close()

        return Ticket(
            token,
            accountId,
            characterId,
            sourceId,
            targetId,
            false,
            expireAt
        )
    }

    override fun punch(ticket: Ticket) {
        val stmt = playersDb.createPreparedStatement(UPD_TICKET_PUNCH)!!

        stmt.setString(1, ticket.token)
        stmt.execute()
        stmt.close()

        ticket.isPunched = true
    }

    private fun ticketFromResultSet(rs: ResultSet): Ticket = Ticket(
        rs.getString(1),
        rs.getInt(2),
        rs.getInt(3),
        getIntOrNull(rs, 4),
        rs.getInt(5),
        rs.getBoolean(6),
        rs.getTimestamp(7)
    )

    companion object {
        const val SEL_TICKET_BY_TOKEN =
            "SELECT token, account_id, character_id, source_id, target_id, is_punched, expire_at FROM tickets WHERE token = ?;"
        const val SEL_TICKET_BY_ACCOUNT_ID =
            "SELECT token, account_id, character_id, source_id, target_id, is_punched, expire_at FROM tickets WHERE account_id = ?;"

        const val INS_TICKET =
            "INSERT INTO tickets (token, account_id, character_id, source_id, target_id, is_punched, expire_at) VALUES (?, ?, ?, ?, ?, ?, ?);"

        const val UPD_TICKET_PUNCH = "UPDATE tickets SET is_punched = 1 WHERE token = ?;"

        const val DEL_TICKET_BY_ACCOUNT_ID = "DELETE FROM tickets WHERE account_id = ?;"
    }
}
