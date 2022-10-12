package fr.rob.world.service.packages.common.domain.exception

open class DomainException : Exception {
    constructor(message: String) : super(message, null)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
