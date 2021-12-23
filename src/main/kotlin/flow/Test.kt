package flow

import kotlinx.coroutines.*

fun main() = runBlocking {
    repeat(100_000) { // launch a lot of coroutines
        launch {
            withContext(Dispatchers.Default) {
                delay(5000L)
                print("${Thread.currentThread().name} print . \n")
            }
        }
    }
}