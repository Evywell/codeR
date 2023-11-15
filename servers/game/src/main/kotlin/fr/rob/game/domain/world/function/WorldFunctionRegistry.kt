package fr.rob.game.domain.world.function

class WorldFunctionRegistry(functionsToRegister: Array<WorldFunctionItem>) {
    private val functions = HashMap<Int, WorldFunctionInterface>()

    init {
        for (functionToRegister in functionsToRegister) {
            functions[functionToRegister.opcode] = functionToRegister.function
        }
    }

    fun getFunction(opcode: Int): WorldFunctionInterface = functions[opcode]!!

    data class WorldFunctionItem(val opcode: Int, val function: WorldFunctionInterface)
}
