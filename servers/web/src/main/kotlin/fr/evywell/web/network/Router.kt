package fr.evywell.web.network

import io.netty.handler.codec.http.HttpMethod

class Router {

    private val routes: MutableList<Route> = ArrayList()

    fun addRoute(route: Route) {
        routes.add(route)
    }

    fun match(method: HttpMethod, uri: String): Route? {
        for (route in routes) {
            if (route.match(method, uri)) {
                return route
            }
        }

        return null
    }
}

data class Route(
    private val method: HttpMethod,
    private val path: String,
    val handler: RouteHandler,
    private val parameters: Map<String, String>? = null
) {

    private var regexPath: String? = null

    fun match(method: HttpMethod, uri: String): Boolean {
        return this.method == method && matchPath(uri)
    }

    companion object {
        const val PARAM_REGEX = "\\{([A-Za-z0-9]+)\\}"
    }

    private fun matchPath(uri: String): Boolean {
        if (regexPath == null && parameters !== null) {
            parseRouteParams()
        }

        return false
    }

    private fun parseRouteParams() {
        val paramRegex = PARAM_REGEX.toRegex()
        val paramMatchResult = paramRegex.find(path, 0)
        regexPath = path

        if (paramMatchResult == null) {
            // There is no parameter for this route
            return
        }

        val values = paramMatchResult.groupValues.subList(1, paramMatchResult.groupValues.size)

        for (value in values) {
            if (!parameters!!.containsKey(value)) {
                throw Exception("There is no parameter $value in the parameter list")
            }

            replaceParameterByPathRegex(value, parameters[value]!!)
        }
    }

    private fun replaceParameterByPathRegex(parameterName: String, parameterRegex: String) {
        regexPath = regexPath?.replace("\\{$parameterName\\}", "($parameterRegex)")
    }
}
