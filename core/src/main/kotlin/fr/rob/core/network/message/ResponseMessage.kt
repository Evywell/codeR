package fr.rob.core.network.message

import com.google.protobuf.Message

data class ResponseMessage(val opcode: Int, val message: Message?)
