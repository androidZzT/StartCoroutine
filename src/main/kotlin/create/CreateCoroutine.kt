package create

import kotlin.coroutines.*

fun main() {
    createAndExecute()
}

fun createAndExecute() {
    //1. 创建协程体
    val coroutine = suspend {
        println("in coroutine")
        5
    }.createCoroutine(object: Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("coroutine end: $result")
        }

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

    })

    //2. 执行协程
    coroutine.resume(Unit)
}

fun startImmediately() {
    suspend {
        println("in coroutine")
        5
    }.startCoroutine(object: Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("coroutine end: $result")
        }

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

    })
}