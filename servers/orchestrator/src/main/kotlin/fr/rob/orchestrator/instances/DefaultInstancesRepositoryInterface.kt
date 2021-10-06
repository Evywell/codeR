package fr.rob.orchestrator.instances

import fr.rob.orchestrator.nodes.GameNode

interface DefaultInstancesRepositoryInterface {

    fun getDefaultInstancesByNode(node: GameNode): List<DefaultInstance>
}
