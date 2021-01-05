package fr.rob.test.domain.process

import fr.rob.game.domain.process.ProcessManager
import fr.rob.test.sandbox.process.BasicProcess
import org.junit.Assert.assertEquals
import org.junit.Test

class ProcessManagerTest {

    @Test
    fun `tout va bien`() {
        val pm = ProcessManager()
        pm.registerProcess(BasicProcess::class) { BasicProcess("Jean") }

        val p1: BasicProcess = pm.makeProcess(BasicProcess::class) as BasicProcess
        val p2: BasicProcess = pm.makeProcess(BasicProcess::class) as BasicProcess

        p2.name = "Axel"
        assertEquals(p1.name, "Jean")
        assertEquals(p2.name, "Axel")
    }
}
