package fr.rob.shared.ticket

import java.util.Date

interface TicketRepositoryInterface {

    fun byToken(token: String): Ticket?
    fun byAccountId(accountId: Int): Ticket?
    fun removeByAccountId(accountId: Int)
    fun insert(token: String, accountId: Int, characterId: Int, sourceId: Int?, targetId: Int, expireAt: Date): Ticket
    fun punch(ticket: Ticket)
}
