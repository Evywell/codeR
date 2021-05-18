package fr.rob.login.test.feature.service.store

import fr.rob.entities.CharacterStandProtos

class CharacterStore : Store() {

    var stand: CharacterStandProtos.CharacterStand? = null
    var mainCharacter: CharacterStandProtos.CharacterStand.Character? = null

    fun setCharacterStand(stand: CharacterStandProtos.CharacterStand) {
        this.stand = stand

        for (character in stand.charactersList) {
            if (character.id == stand.currentCharacterId) {
                mainCharacter = character
                break
            }
        }
    }
}
