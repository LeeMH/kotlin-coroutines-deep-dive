package ch10

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = SupervisorJob()
    launch(job) {
        delay(1000)
        throw Error("some error")
    }

    launch(job) {
        delay(2000)
        println("will be printed")
    }

    job.complete()
    job.join()
}