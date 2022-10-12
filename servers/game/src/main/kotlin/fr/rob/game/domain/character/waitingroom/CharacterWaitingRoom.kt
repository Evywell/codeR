package fr.rob.game.domain.character.waitingroom

import fr.rob.game.domain.instance.MapInstance

class CharacterWaitingRoom {
    private val waiters = HashMap<String, CharacterInRoom>()

    fun enter(characterId: Int, mapInstance: MapInstance, accountId: String) {
        waiters[accountId] = CharacterInRoom(characterId, mapInstance)
    }

    data class CharacterInRoom(val characterId: Int, val mapInstance: MapInstance)
}
