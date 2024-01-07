package ch8

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val parentJob = Job()
    val job = Job(parentJob)

    launch(job) {
        delay(1000)
        println("job1!! ")
    }

    launch(job) {
        delay(2000)
        println("job2!! never printed")
    }

    delay(1100)
    parentJob.cancel()

    job.children.forEach {
        println(it)
        it.join()
    }

    println("done")
}