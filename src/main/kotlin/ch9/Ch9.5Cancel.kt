package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delay(200)
            println("coroutine finished")
        } finally {
            println("finally")
            withContext(NonCancellable) {
                delay(1000)
                println("will be printed")
            }
        }
    }

    delay(100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}