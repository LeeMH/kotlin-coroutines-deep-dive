package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delay(2000)
        } finally {
            println("finally")
            launch { // 무시된다.
                println("will not be printed")
            }
            delay(1000) // 예외발생!!
            println("will not be printed")
        }
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
}