package fr.rob.core.network

abstract class Filter<T> {

    abstract fun process(subject: T): Boolean
}
