package fr.rob.shared.ticket

import fr.rob.core.misc.Time
import org.apache.commons.lang3.RandomStringUtils
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class TicketProcess(private val ticketRepository: TicketRepositoryInterface) {

    fun create(accountId: Int, characterId: Int, sourceId: Int?, target: Int): TicketState {
        val existingTicket = ticketRepository.byAccountId(accountId)

        if (existingTicket != null && existingTicket.isPunched) {
            return getTicketErrorState(ERR_TICKET_ALREADY_EXISTS)
        }

        if (existingTicket != null) {
            ticketRepository.removeByAccountId(accountId)
        }

        val expiration = getExpirationDate()
        val token = generateToken()

        val ticket = ticketRepository.insert(token, accountId, characterId, sourceId, target, expiration)

        return TicketState(false, ticket = ticket)
    }

    fun verify(token: String): TicketState {
        val ticket = ticketRepository.byToken(token) ?: return getTicketErrorState(ERR_TICKET_NOT_FOUND)

        if (ticket.isPunched || ticket.isExpired()) {
            return getTicketErrorState(ERR_TICKET_USED_OR_EXPIRED)
        }

        // Punch the ticket to make it usable once
        ticketRepository.punch(ticket)

        return TicketState(false, ticket = ticket)
    }

    private fun getExpirationDate(): Date = Time.addMinutes(EXPIRATION_IN_MINUTES)

    private fun generateToken(): String = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH)

    private fun getTicketErrorState(error: String): TicketState =
        TicketState(true, error)

    companion object {
        const val EXPIRATION_IN_MINUTES = 10L
        const val TOKEN_LENGTH = 40

        const val ERR_TICKET_ALREADY_EXISTS = "err_ticket_already_exists"
        const val ERR_TICKET_NOT_FOUND = "err_ticket_not_found"
        const val ERR_TICKET_USED_OR_EXPIRED = "err_ticket_used_or_expired"
    }

    data class TicketState(var hasError: Boolean, var error: String? = null, var ticket: Ticket? = null)
}
