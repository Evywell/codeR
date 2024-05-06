package fr.rob.game.infra.network.physic

import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface

class PhysicOpcodeFunctionRegistry(private val functions: Array<OpcodeFunctionRegistryInterface.OpcodeFunctionItem<PhysicFunctionParameters>>) : OpcodeFunctionRegistryInterface<PhysicFunctionParameters> {
    override fun getOpcodeFunctions(): Array<OpcodeFunctionRegistryInterface.OpcodeFunctionItem<PhysicFunctionParameters>> = functions
}
