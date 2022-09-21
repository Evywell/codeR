package fr.rob.core.opcode.v2

interface OpcodeFunctionRegistryInterface<FunctionParameters> {
    fun getOpcodeFunctions(): Array<OpcodeFunctionItem<FunctionParameters>>

    data class OpcodeFunctionItem<FunctionParameters>(
        val opcode: Int,
        val function: OpcodeFunctionInterface<FunctionParameters>
    )
}
