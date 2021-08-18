package fr.rob.game.test.unit.domain.process

import fr.rob.core.process.ProcessManager
import fr.rob.game.test.unit.sandbox.process.CreateProcessWithArgumentsProcess
import fr.rob.game.test.unit.sandbox.process.CreateProcessWithInterfaceInterface
import fr.rob.game.test.unit.sandbox.process.CreateProcessWithInterfaceProcess
import fr.rob.game.test.unit.sandbox.process.GetSameProcessProcess
import fr.rob.game.test.unit.sandbox.process.ProcessShouldBeIndependentProcess
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test

class ProcessManagerTest {

    @Test
    fun `same processes should be independents`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(ProcessShouldBeIndependentProcess::class) { ProcessShouldBeIndependentProcess("Jean") }

        // Act
        val p1: ProcessShouldBeIndependentProcess = pm.makeProcess(ProcessShouldBeIndependentProcess::class)
        val p2: ProcessShouldBeIndependentProcess = pm.makeProcess(ProcessShouldBeIndependentProcess::class)

        p2.name = "Axel"

        // Assert
        assertEquals("Jean", p1.name)
        assertEquals("Axel", p2.name)
    }

    @Test
    fun `create process with arguments`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(CreateProcessWithArgumentsProcess::class) { args -> CreateProcessWithArgumentsProcess(args!![0] as DependencyClass) }

        // Act
        val process = pm.makeProcess(CreateProcessWithArgumentsProcess::class, arrayOf(DependencyClass()))

        // Assert
        assertEquals("Hello", process.dependency.sayHello())
    }

    @Test
    fun `create process using interface`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(CreateProcessWithInterfaceInterface::class) { CreateProcessWithInterfaceProcess() }

        // Act
        val process = pm.makeProcess(CreateProcessWithInterfaceInterface::class)

        // Assert
        assertEquals(true, process is CreateProcessWithInterfaceProcess)
    }

    @Test
    fun `get same process`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(GetSameProcessProcess::class) { GetSameProcessProcess() }

        // Act
        val process1 = pm.getOrMakeProcess(GetSameProcessProcess::class)
        process1.mutableVar = 1

        val process2 = pm.getOrMakeProcess(GetSameProcessProcess::class)

        // Assert
        assertEquals(1, process1.mutableVar)
        assertEquals(1, process2.mutableVar)
    }
}

class DependencyClass {

    fun sayHello() = "Hello"
}
