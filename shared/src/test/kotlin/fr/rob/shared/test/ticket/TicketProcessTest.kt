package fr.rob.shared.test.ticket

import fr.rob.shared.ticket.Ticket
import fr.rob.shared.ticket.TicketProcess
import fr.rob.shared.ticket.TicketProcess.Companion.ERR_TICKET_ALREADY_EXISTS
import fr.rob.shared.ticket.TicketProcess.Companion.TOKEN_LENGTH
import fr.rob.shared.ticket.TicketRepositoryInterface
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test
import org.mockito.Mockito.* // ktlint-disable no-wildcard-imports
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class TicketProcessTest {

    @Test
    fun `verify an unknown token`() {
        // Arrange
        val token = "123ticket123"

        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byToken(token)).thenReturn(null)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.verify(token)

        // Assert
        assertTrue(state.hasError)
        assertEquals(TicketProcess.ERR_TICKET_NOT_FOUND, state.error)
    }

    @Test
    fun `verify punched token`() {
        // Arrange
        val token = "123ticket123"
        val ticket = Ticket(token, 1, 1, 1, 1, true, Date())

        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byToken(token)).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.verify(token)

        // Assert
        assertTrue(state.hasError)
        assertEquals(TicketProcess.ERR_TICKET_USED_OR_EXPIRED, state.error)
    }

    @Test
    fun `verify expired token`() {
        // Arrange
        val token = "123ticket123"
        val ticket = Ticket(token, 1, 1, 1, 1, false, nowPlusMinutes(-30))

        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byToken(token)).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.verify(token)

        // Assert
        assertTrue(state.hasError)
        assertEquals(TicketProcess.ERR_TICKET_USED_OR_EXPIRED, state.error)
    }

    @Test
    fun `verify a valid token`() {
        // Arrange
        val token = "123ticket123"
        val ticket = Ticket(token, 1, 1, 1, 1, false, nowPlusMinutes(2))

        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byToken(token)).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.verify(token)

        // Assert
        assertFalse(state.hasError)
    }

    @Test
    fun `correct expiration date`() {
        // Arrange
        val ticketRepository = mock<TicketRepositoryInterface>()
        val process = TicketProcess(ticketRepository)

        // Act
        val method = process::class.java.getDeclaredMethod("getExpirationDate")
        method.isAccessible = true

        val expirationDate = method.invoke(process) as Date

        // Assert
        assertTrue(expirationDate > Date())
    }

    @Test
    fun `generate token with X characters`() {
        // Arrange
        val ticketRepository = mock<TicketRepositoryInterface>()
        val process = TicketProcess(ticketRepository)

        // Act
        val method = process::class.java.getDeclaredMethod("generateToken")
        method.isAccessible = true

        val token = method.invoke(process) as String

        // Assert
        assertEquals(TOKEN_LENGTH, token.length)
    }

    @Test
    fun `create a ticket`() {
        // Arrange
        val ticket = Ticket("123ticket123", 1, 2, null, 5, true, nowPlusMinutes(20))
        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byAccountId(1)).thenReturn(null)
        `when`(ticketRepository.insert(anyString(), anyInt(), anyInt(), anyOrNull(), anyInt(), anyOrNull())).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.create(1, 2, null, 5)

        // Assert
        assertFalse(state.hasError)
    }

    @Test
    fun `create ticket with already existing ticket not punched`() {
        // Arrange
        val ticket = Ticket("123ticket123", 1, 2, null, 5, false, nowPlusMinutes(20))
        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byAccountId(1)).thenReturn(ticket)
        `when`(ticketRepository.insert(anyString(), anyInt(), anyInt(), anyOrNull(), anyInt(), anyOrNull())).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.create(1, 2, null, 5)

        // Assert
        assertFalse(state.hasError)
        verify(ticketRepository, times(1)).removeByAccountId(1)
    }

    @Test
    fun `attempt to create a ticket with already existing ticket`() {
        // Arrange
        val ticket = Ticket("123ticket123", 1, 2, null, 5, true, nowPlusMinutes(20))
        val ticketRepository = mock<TicketRepositoryInterface>()
        `when`(ticketRepository.byAccountId(1)).thenReturn(ticket)

        val process = TicketProcess(ticketRepository)

        // Act
        val state = process.create(1, 2, null, 5)

        // Assert
        assertTrue(state.hasError)
        assertEquals(ERR_TICKET_ALREADY_EXISTS, state.error)
    }

    private fun nowPlusMinutes(minutes: Int): Date {
        val dateTime = LocalDateTime.now().plus(Duration.of(minutes.toLong(), ChronoUnit.MINUTES))

        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant())
    }
}
