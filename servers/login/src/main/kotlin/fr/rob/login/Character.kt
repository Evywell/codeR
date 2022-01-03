package fr.rob.login

import fr.rob.entities.CharacterProtos

data class Character(val id: Int, val name: String) :
    EntityToMessageInterface<CharacterProtos.Character> {

    override fun build(): CharacterProtos.Character = CharacterProtos.Character.newBuilder()
        .setId(id)
        .setName(name)
        .build()

    companion object : MessageToEntityInterface<CharacterProtos.Character, Character> {

        override fun fromMessage(message: CharacterProtos.Character): Character = Character(message.id, message.name)
    }
}
