package fr.rob.web.network

abstract class RouteHandler {

    abstract fun handle(request: Request): String?
}
