package basic.contract

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data class Message(val title: String)

@ExperimentalContracts
fun Message?.isMessageValid(): Boolean {
    contract {
       returns(true) implies(this@isMessageValid is Message)
    }
    return this != null && this.title.isNotEmpty()
}

@ExperimentalContracts
fun testMessageIsValid(msg: Message?) {
    if (msg.isMessageValid()) {
        println(msg.title) //Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type Message?
    }
}

@ExperimentalContracts
fun testRunSafe() {
    val a: Int
   runSafe {
        a = 100
    }
}

@ExperimentalContracts
inline fun runSafe(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        block.invoke()
    } catch (e : Exception) {
        println(e.message)
    }
}