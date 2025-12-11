package fr.rob.game.player

import fr.rob.game.instance.MapInstance

interface InstanceFinderInterface {
    fun fromCharacterId(characterId: Int): MapInstance
}
