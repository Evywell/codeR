package fr.rob.orchestrator.api.instance

import fr.rob.orchestrator.api.node.Node

data class InstanceContainer(val instance: Instance, var node: Node?)
