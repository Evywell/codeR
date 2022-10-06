package fr.rob.gateway.extension.realm.opcode

import fr.rob.core.network.Packet

data class RealmFunctionParameters(
    val opcode: Int,
    val packet: Packet,
)
