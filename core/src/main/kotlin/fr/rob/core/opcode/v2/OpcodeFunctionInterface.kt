package fr.rob.core.opcode.v2

interface OpcodeFunctionInterface<FunctionParameters> {
    fun call(functionParameters: FunctionParameters)
    fun isCallAuthorized(functionParameters: FunctionParameters): Boolean
}
