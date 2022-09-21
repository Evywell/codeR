package fr.rob.core.opcode.v2

import fr.rob.core.opcode.v2.exception.OpcodeFunctionCallUnauthorizedException
import fr.rob.core.opcode.v2.exception.OpcodeFunctionNotFoundException

class OpcodeHandler<FunctionParameters>(opcodeRegistry: OpcodeFunctionRegistryInterface<FunctionParameters>) {
    private val opcodeFunctions = HashMap<Int, OpcodeFunctionInterface<FunctionParameters>>()

    init {
        for (opcodeFunctionItem in opcodeRegistry.getOpcodeFunctions()) {
            opcodeFunctions[opcodeFunctionItem.opcode] = opcodeFunctionItem.function
        }
    }

    /**
     * @throws OpcodeFunctionCallUnauthorizedException
     * @throws OpcodeFunctionNotFoundException
     */
    fun process(opcode: Int, parameters: FunctionParameters) {
        val function = getFunction(opcode)

        if (!function.isCallAuthorized(parameters)) {
            throw OpcodeFunctionCallUnauthorizedException()
        }

        function.call(parameters)
    }

    fun getFunction(opcode: Int): OpcodeFunctionInterface<FunctionParameters> {
        return opcodeFunctions[opcode] ?: throw OpcodeFunctionNotFoundException()
    }
}
