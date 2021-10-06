package fr.rob.orchestrator.instances.request

import java.util.UUID

class InstanceRequestGenerator : InstanceRequestGeneratorInterface {

    override fun generate(): String = UUID.randomUUID().toString()
}
