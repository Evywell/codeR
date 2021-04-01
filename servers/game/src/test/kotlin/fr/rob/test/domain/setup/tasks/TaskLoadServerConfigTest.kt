package fr.rob.test.domain.setup.tasks

import fr.rob.game.domain.network.Server
import fr.rob.game.domain.setup.AppSetup
import fr.rob.game.domain.tasks.TaskLoadServerConfig as Task
import fr.rob.test.sandbox.tasks.LoadServerRepository
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class TaskLoadServerConfigTest {

    @Test
    fun `run task with correct config`() {
        // Arrange
        val repository = LoadServerRepository()
        val setup = AppSetup()

        val servers: Array<Server> = arrayOf(
            Server("srvname1", "seraddr1", 1),
            Server("srvname2", "seraddr2", 2),
            Server("srvname3", "seraddr3", 3)
        )

        // Act
        val task = Task(servers, repository, setup)
        task.run()

        val s = setup.getServers()

        // Assert
        assertEquals("srvname1", s[0].serverName)
        assertEquals("seraddr1", s[0].serverAddress)
        assertEquals(1, s[0].mapId)
        assertEquals(4, s[0].zones.size)

        assertEquals("srvname2", s[1].serverName)
        assertEquals("seraddr2", s[1].serverAddress)
        assertEquals(2, s[1].mapId)
        assertEquals(4, s[1].zones.size)

        assertEquals("srvname3", s[2].serverName)
        assertEquals("seraddr3", s[2].serverAddress)
        assertEquals(3, s[2].mapId)
        assertEquals(4, s[2].zones.size)
    }

    @Test
    fun `run task using server without zones`() {
        // Arrange
        val repository = LoadServerRepository()
        val setup = AppSetup()

        val servers: Array<Server> = arrayOf(
            Server("srvname1", "seraddr1", 0)
        )

        val task = Task(servers, repository, setup)

        // Act & Assert
        Assertions.assertThrows(RuntimeException::class.java) {
            task.run()
        }
    }
}
