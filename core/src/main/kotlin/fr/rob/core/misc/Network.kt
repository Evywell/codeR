package fr.rob.core.misc

class Network {

    companion object {

        fun getAddress(address: String): NetworkAddress {
            val parts = address.split(':')

            return NetworkAddress(parts[0], parts[1].toInt())
        }
    }

    data class NetworkAddress(val ip: String, val port: Int)
}
