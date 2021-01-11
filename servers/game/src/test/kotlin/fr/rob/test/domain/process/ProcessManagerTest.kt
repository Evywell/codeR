package fr.rob.test.domain.process

import fr.rob.game.domain.process.ProcessManager
import fr.rob.test.sandbox.process.CreateProcessWithArgumentsProcess
import fr.rob.test.sandbox.process.ProcessShouldBeIndependentProcess
import org.junit.Assert.assertEquals
import org.junit.Test

class ProcessManagerTest {

    @Test
    fun `same processes should be independents`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(ProcessShouldBeIndependentProcess::class) { ProcessShouldBeIndependentProcess("Jean") }

        // Act
        val p1: ProcessShouldBeIndependentProcess =
            pm.makeProcess(ProcessShouldBeIndependentProcess::class) as ProcessShouldBeIndependentProcess
        val p2: ProcessShouldBeIndependentProcess =
            pm.makeProcess(ProcessShouldBeIndependentProcess::class) as ProcessShouldBeIndependentProcess

        p2.name = "Axel"

        // Assert
        assertEquals("Jean", p1.name)
        assertEquals("Axel", p2.name)
    }

    @Test
    fun `create process with arguments`() {
        // Arrange
        val pm = ProcessManager()
        pm.registerProcess(CreateProcessWithArgumentsProcess::class)
        { args -> CreateProcessWithArgumentsProcess(args!![0] as DependencyClass) }

        // Act
        val process = pm.makeProcess(CreateProcessWithArgumentsProcess::class, arrayOf(DependencyClass()))
                as CreateProcessWithArgumentsProcess

        // Assert
        assertEquals("Hello", process.dependency.sayHello())
    }
}

class DependencyClass {

    fun sayHello() = "Hello"
}
