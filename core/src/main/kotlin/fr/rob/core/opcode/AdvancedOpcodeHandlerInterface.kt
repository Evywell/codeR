package fr.rob.core.opcode

import fr.rob.core.process.ProcessManager
import java.util.ArrayList
import kotlin.reflect.KClass

interface AdvancedOpcodeHandlerInterface : OpcodeHandlerInterface {

    var processManager: ProcessManager

    fun registerAutowiredOpcode(opcode: Int, opcodeFunction: KClass<*>) {
        val javaClass = opcodeFunction.java
        val constructors = javaClass.constructors

        if (constructors.isEmpty()) {
            registerOpcode(opcode, javaClass.getDeclaredConstructor().newInstance() as OpcodeFunction)

            return
        }

        val constructor = constructors[0]
        val constructorParametersTypes = constructor.parameterTypes
        val parameters = ArrayList<Any>()

        for (parameterType in constructorParametersTypes) {
            parameters.add(processManager.getOrMakeProcess(parameterType.kotlin))
        }

        // The start is used to convert an array to a spread operator (*parameters.toTypedArray())
        registerOpcode(opcode, constructor.newInstance(*parameters.toTypedArray()) as OpcodeFunction)
    }
}
