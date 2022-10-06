package fr.rob.gateway.extension.game.opcode

import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface
import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface.OpcodeFunctionItem
import fr.rob.gateway.extension.game.GameNodeClient
import fr.rob.gateway.extension.game.action.GameInitializationSucceedOpcodeFunction

class GameNodeOpcodeRegistry(
    private val gameNodeClient: GameNodeClient
) : OpcodeFunctionRegistryInterface<GameNodeFunctionParameters> {
    override fun getOpcodeFunctions(): Array<OpcodeFunctionItem<GameNodeFunctionParameters>> =
        arrayOf(
            OpcodeFunctionItem(GAME_INITIALIZATION_SUCCEED, GameInitializationSucceedOpcodeFunction(gameNodeClient)),
        )
}
