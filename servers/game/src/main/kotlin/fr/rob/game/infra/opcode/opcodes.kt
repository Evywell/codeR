package fr.rob.game.infra.opcode

const val CMSG_LOG_INTO_WORLD = 0x01
const val SMSG_PLAYER_DESCRIPTION = 0x02
const val SMSG_NEARBY_OBJECT_UPDATE = 0x03
const val SMSG_MOVEMENT_HEARTBEAT = 0x04
const val CMSG_REMOVE_FROM_WORLD = 0x05
const val CMSG_PLAYER_MOVEMENT = 0x06
const val CMSG_PLAYER_CAST_SPELL = 0x07
const val SMSG_OBJECT_HEALTH_UPDATED = 0x08
const val CMSG_PLAYER_ENGAGE_COMBAT = 0x09
const val SMSG_OBJECT_MOVING_TO_DESTINATION = 0x0A
const val SMSG_DEBUG_SIGNAL = 0x97
const val CMSG_CHEAT_TELEPORT = 0x98
const val SMSG_GAME_INITIALIZATION_SUCCEED = 0x99

val OPCODES_MAP = Array(0x99) { i ->
    when (i) {
        CMSG_LOG_INTO_WORLD -> "CMSG_LOG_INTO_WORLD"
        SMSG_PLAYER_DESCRIPTION -> "SMSG_PLAYER_DESCRIPTION"
        SMSG_NEARBY_OBJECT_UPDATE -> "SMSG_NEARBY_OBJECT_UPDATE"
        SMSG_MOVEMENT_HEARTBEAT -> "SMSG_MOVEMENT_HEARTBEAT"
        CMSG_REMOVE_FROM_WORLD -> "CMSG_REMOVE_FROM_WORLD"
        CMSG_PLAYER_MOVEMENT -> "CMSG_PLAYER_MOVEMENT"
        CMSG_PLAYER_CAST_SPELL -> "CMSG_PLAYER_CAST_SPELL"
        SMSG_OBJECT_HEALTH_UPDATED -> "SMSG_OBJECT_HEALTH_UPDATED"
        SMSG_DEBUG_SIGNAL -> "SMSG_DEBUG_SIGNAL"
        CMSG_CHEAT_TELEPORT -> "CMSG_CHEAT_TELEPORT"
        CMSG_PLAYER_ENGAGE_COMBAT -> "CMSG_PLAYER_ENGAGE_COMBAT"
        SMSG_GAME_INITIALIZATION_SUCCEED -> "SMSG_GAME_INITIALIZATION_SUCCEED"
        SMSG_OBJECT_MOVING_TO_DESTINATION -> "SMSG_OBJECT_MOVING_TO_DESTINATION"
        else -> "UNKNOWN"
    }
}
