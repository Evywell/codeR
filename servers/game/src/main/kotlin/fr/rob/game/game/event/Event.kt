package fr.rob.game.game.event

abstract class Event {

    var addTime: Long = 0
    var execTime: Long = 0

    abstract fun execute()
}
