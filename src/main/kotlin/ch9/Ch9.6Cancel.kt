package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        delay(1000)
        println("coroutine finished")
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}