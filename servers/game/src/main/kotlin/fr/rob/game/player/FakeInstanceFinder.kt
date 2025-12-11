package fr.rob.game.player

import fr.rob.game.instance.InstanceManager
import fr.rob.game.instance.MapInstance
import fr.rob.game.player.InstanceFinderInterface

class FakeInstanceFinder(private val instanceManager: InstanceManager) : InstanceFinderInterface {
    override fun fromCharacterId(characterId: Int): MapInstance = instanceManager.retrieve(1) // @todo replace this
}
