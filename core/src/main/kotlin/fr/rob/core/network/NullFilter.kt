package fr.rob.core.network

class NullFilter<T> : Filter<T>() {
    override fun process(subject: T): Boolean = true
}
