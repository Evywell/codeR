package fr.rob.login

interface MessageToEntityInterface<Message, Entity> {

    fun fromMessage(message: Message): Entity
}
