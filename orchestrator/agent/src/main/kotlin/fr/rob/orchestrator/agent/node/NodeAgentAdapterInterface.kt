package fr.rob.orchestrator.agent.node

interface NodeAgentAdapterInterface {

    fun handleNewInstanceRequest(nodeName: String, mapId: Int, zoneId: Int?)
}
