package fr.rob.core.helper

fun env(name: String, default: Any? = null): Any? {
    return System.getenv(name) ?: return default
}
