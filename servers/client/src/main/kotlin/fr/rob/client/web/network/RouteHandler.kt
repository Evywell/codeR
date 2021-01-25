package fr.rob.client.web.network


abstract class RouteHandler {

    abstract fun handle(request: Request): String?
}
