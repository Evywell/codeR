package fr.rob.game.domain.character.waitingroom

import fr.rob.game.domain.instance.MapInstance
import java.util.Optional

class CharacterWaitingRoom {
    private val waiters = HashMap<String, CharacterInRoom>()

    fun enter(characterId: Int, mapInstance: MapInstance, accountId: String) {
        waiters[accountId] = CharacterInRoom(characterId, mapInstance)
    }

    fun leave(accountId: String): Optional<CharacterInRoom> =
        Optional.ofNullable(waiters[accountId])

    fun isWaiting(accountId: String): Boolean = waiters.containsKey(accountId)

    data class CharacterInRoom(val characterId: Int, val mapInstance: MapInstance)
}
