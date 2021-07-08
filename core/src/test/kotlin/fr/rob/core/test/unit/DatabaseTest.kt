package fr.rob.core.test.unit

import fr.rob.core.database.Connection
import fr.rob.core.infrastructure.database.PreparedStatement
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import java.sql.ResultSet

abstract class DatabaseTest {

    protected lateinit var dbMock: Connection
    protected lateinit var stmtMock: PreparedStatement
    protected lateinit var rsMock: ResultSet

    @BeforeEach
    fun setUp() {
        dbMock = mock()
        stmtMock = mock()
        rsMock = mock()

        `when`(stmtMock.execute()).thenReturn(true)
        `when`(stmtMock.executeUpdate()).thenReturn(1)
        `when`(stmtMock.resultSet).thenReturn(rsMock)
        `when`(stmtMock.generatedKeys).thenReturn(rsMock)
    }
}
