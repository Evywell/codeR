package fr.rob.login.test.feature.service.store

import kotlin.reflect.KClass

class StoreManager {

    private val stores = HashMap<String, Store>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getStore(fqn: KClass<out T>): T {
        return stores[fqn.qualifiedName]!! as T
    }

    fun setStore(store: Store) {
        stores[store::class.qualifiedName!!] = store
    }
}
