package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        repeat(1_000) { num ->
            delay(200)
            println("num $num")
        }
    }

    delay(1100)
    job.cancel()
    job.join()
    println("Cancelled successfully")
}