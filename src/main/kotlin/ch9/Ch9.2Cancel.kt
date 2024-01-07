package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        repeat(1_000) { num ->
            delay(100)
            Thread.sleep(100)
            println("num $num")
        }
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
}