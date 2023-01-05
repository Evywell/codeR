package fr.rob.game.infra.opcode

import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface
import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface.OpcodeFunctionItem

class OpcodeRegistry(private val functions: Array<OpcodeFunctionItem<GameNodeFunctionParameters>>) :
    OpcodeFunctionRegistryInterface<GameNodeFunctionParameters> {
    override fun getOpcodeFunctions(): Array<OpcodeFunctionItem<GameNodeFunctionParameters>> = functions
}
