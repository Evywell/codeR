package fr.rob.core.network

interface ServerFactoryInterface {

    fun build(port: Int, name: String, ssl: Boolean = false): Server
}
