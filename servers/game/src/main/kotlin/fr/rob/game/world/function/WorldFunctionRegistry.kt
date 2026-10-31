package fr.rob.game.world.function

class WorldFunctionRegistry(functionsToRegister: Array<WorldFunctionItem>) {
    private val functions = HashMap<Int, WorldFunctionInterface>()

    init {
        for ((opcode, function) in functionsToRegister) {
            functions[opcode] = function
        }
    }

    fun findFunction(opcode: Int): WorldFunctionInterface? = functions[opcode]

    data class WorldFunctionItem(val opcode: Int, val function: WorldFunctionInterface)
}
