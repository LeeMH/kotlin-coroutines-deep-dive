package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            repeat(1_000) { num ->
                delay(100)
                println("num $num")
            }
        } catch (e: CancellationException) {
            println(e)
            throw e
        } finally {
            println("need to close all resources")
        }
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}