package fr.rob.game.world.function

import fr.rob.game.player.session.SessionState

class WorldFunctionRegistry(functionsToRegister: Array<WorldFunctionItem>) {
    private val functions = HashMap<Int, WorldFunctionItem>()

    init {
        for (item in functionsToRegister) {
            functions[item.opcode] = item
        }
    }

    fun findItem(opcode: Int): WorldFunctionItem? = functions[opcode]

    fun findFunction(opcode: Int): WorldFunctionInterface? = functions[opcode]?.function

    data class WorldFunctionItem(
        val opcode: Int,
        val function: WorldFunctionInterface,
        val requiredState: SessionState,
    )
}
