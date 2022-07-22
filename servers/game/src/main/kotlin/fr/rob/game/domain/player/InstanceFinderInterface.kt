package fr.rob.game.domain.player

import fr.rob.game.domain.instance.MapInstance

interface InstanceFinderInterface {
    fun fromCharacterId(characterId: Int): MapInstance
}
