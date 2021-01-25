package fr.rob.client.web.network

import io.netty.handler.codec.http.HttpMethod

class Router {

    private val routes: MutableList<Route> = ArrayList()

    fun addRoute(route: Route) {
        routes.add(route)
    }

    fun match(method: HttpMethod, uri: String, request: Request): Route? {
        for (route in routes) {
            if (route.match(method, uri, request)) {
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
    private var parsedParameters = HashMap<Int, String>()

    fun match(method: HttpMethod, uri: String, request: Request): Boolean {
        val routeParameters = matchPath(uri)
        request.routeParameters = routeParameters

        return this.method == method && routeParameters != null
    }

    companion object {
        const val PARAM_REGEX = "\\{([A-Za-z0-9]+)\\}"
    }

    private fun matchPath(uri: String): RouteParameters? {
        if (regexPath == null && parameters !== null) {
            parseRouteParams()
        }

        val matchResult = regexPath!!.toRegex().find(uri, 0) ?: return null
        val values = matchResult.groupValues.subList(1, matchResult.groupValues.size)

        val routeParameters = RouteParameters()

        for ((i, value) in values.withIndex()) {
            routeParameters.set(parsedParameters[i]!!, value)
        }

        return routeParameters
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

        for ((i, value) in values.withIndex()) {
            if (!parameters!!.containsKey(value)) {
                throw Exception("There is no parameter $value in the parameter list")
            }

            replaceParameterByPathRegex(value, parameters[value]!!, i)
        }
    }

    private fun replaceParameterByPathRegex(parameterName: String, parameterRegex: String, parameterIndex: Int) {
        regexPath = regexPath?.replace("{$parameterName}", "($parameterRegex)")
        parsedParameters[parameterIndex] = parameterName
    }
}

class RouteParameters {

    private val parameters = HashMap<String, Any>()

    fun get(key: String): Any? {
        return parameters[key]
    }

    fun set(key: String, value: Any) {
        parameters[key] = value
    }
}
