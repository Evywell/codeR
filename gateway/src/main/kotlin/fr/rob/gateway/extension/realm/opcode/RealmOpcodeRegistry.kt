package fr.rob.gateway.extension.realm.opcode

import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface
import fr.rob.core.opcode.v2.OpcodeFunctionRegistryInterface.OpcodeFunctionItem
import fr.rob.gateway.extension.realm.RealmService
import fr.rob.gateway.extension.realm.action.BindCharacterToNodeOpcodeFunction
import fr.rob.gateway.extension.realm.gamenode.GameNodes

class RealmOpcodeRegistry(private val gameNodes: GameNodes, private val realmService: RealmService) :
    OpcodeFunctionRegistryInterface<RealmFunctionParameters> {

    override fun getOpcodeFunctions(): Array<OpcodeFunctionItem<RealmFunctionParameters>> = arrayOf(
        OpcodeFunctionItem(SMSG_REALM_BIND_CHARACTER_TO_NODE, BindCharacterToNodeOpcodeFunction(gameNodes, realmService))
    )
}
