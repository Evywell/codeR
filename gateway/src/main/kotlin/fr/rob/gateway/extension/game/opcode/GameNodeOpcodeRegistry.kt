package fr.rob.gateway.extension.game.opcode

import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface
import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface.OpcodeFunctionItem

class GameNodeOpcodeRegistry : OpcodeFunctionRegistryInterface<GameNodeFunctionParameters> {
    override fun getOpcodeFunctions(): Array<OpcodeFunctionItem<GameNodeFunctionParameters>> =
        emptyArray()
}
