package fr.rob.game.test.unit.domain.setup.tasks

import fr.rob.game.domain.network.Server
import fr.rob.game.test.unit.sandbox.tasks.LoadServerRepository
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import fr.rob.game.domain.tasks.TaskLoadServerConfig as Task

class TaskLoadServerConfigTest {

    @Test
    fun `run task with correct config`() {
        // Arrange
        val repository = LoadServerRepository()

        val servers: Array<Server> = arrayOf(
            Server("srvname1", "seraddr1", 1),
            Server("srvname2", "seraddr2", 2),
            Server("srvname3", "seraddr3", 3)
        )

        // Act
        val task = Task(servers, repository)
        task.run()

        // Assert
        assertEquals("srvname1", servers[0].serverName)
        assertEquals("seraddr1", servers[0].serverAddress)
        assertEquals(1, servers[0].mapId)
        assertEquals(4, servers[0].zones.size)

        assertEquals("srvname2", servers[1].serverName)
        assertEquals("seraddr2", servers[1].serverAddress)
        assertEquals(2, servers[1].mapId)
        assertEquals(4, servers[1].zones.size)

        assertEquals("srvname3", servers[2].serverName)
        assertEquals("seraddr3", servers[2].serverAddress)
        assertEquals(3, servers[2].mapId)
        assertEquals(4, servers[2].zones.size)
    }

    @Test
    fun `run task using server without zones`() {
        // Arrange
        val repository = LoadServerRepository()

        val servers: Array<Server> = arrayOf(
            Server("srvname1", "seraddr1", 0)
        )

        val task = Task(servers, repository)

        // Act & Assert
        Assertions.assertThrows(RuntimeException::class.java) {
            task.run()
        }
    }
}
