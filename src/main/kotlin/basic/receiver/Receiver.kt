package basic.receiver

import java.lang.StringBuilder

fun main() {
    println("".buildString {
        this.append("a")
        append("b")
        append("c").let {  }
    })

    println("".buildString_ {sb: StringBuilder ->
        sb.append("d")
        sb.append("e")
        sb.append("f")
    })
}

fun String.buildString(action: StringBuilder.() -> Unit): String {
    val sb = StringBuilder()
    sb.action()
    return sb.toString()
}

fun String.buildString_(action: (stringbuilder: StringBuilder) -> Unit): String {
    val sb = StringBuilder()
    action(sb)
    return sb.toString()
}