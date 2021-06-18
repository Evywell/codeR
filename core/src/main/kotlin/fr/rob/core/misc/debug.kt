package fr.rob.core.misc

import java.lang.StringBuilder

fun dump(variable: Any?) {
    val sb = StringBuilder()
    val stackTrace = Thread.currentThread().stackTrace[2]

    sb.append((stackTrace.fileName ?: "") + " Line: " + stackTrace.lineNumber + " ")

    dumpVar(variable, sb, 0)

    println(sb.toString())
}

private fun dumpList(list: List<*>, sb: StringBuilder, tabs: Int) {
    sb.append(list::class.qualifiedName + "(${list.size}) {\n")

    for (item in list) {
        dumpVar(item, sb, tabs + 1)
    }

    sb.append("}\n")
}

private fun dumpVar(variable: Any?, sb: StringBuilder, tabs: Int = 0) {
    if (variable == null) {
        sb.append("null\n")
    } else if (variable is String) {
        sb.append(variable)
    } else if (variable is List<*>) {
        dumpList(variable, sb, tabs)
    } else {
        dumpObject(variable, sb, tabs)
    }
}

private fun dumpObject(variable: Any, sb: StringBuilder, tabs: Int = 0) {
    sb.append(tabs(tabs) + variable::class.qualifiedName + " {")

    val fields = variable::class.java.declaredFields
    val parentFields = variable::class.java.superclass.declaredFields

    for (field in fields) {
        field.isAccessible = true
        val value = field.get(variable) ?: "null"

        sb.append("\n" + tabs(tabs + 1) + field.name + ": " + value.toString())
    }

    for (field in parentFields) {
        field.isAccessible = true
        val value = field.get(variable) ?: "null"

        sb.append("\n" + tabs(tabs + 1) + field.name + ": " + value.toString())
    }

    sb.append(tabs(tabs) + "}\n")
}

private fun tabs(i: Int): String {
    val sb = StringBuilder()

    for (o in 1..i) {
        sb.append("  ")
    }

    return sb.toString()
}
