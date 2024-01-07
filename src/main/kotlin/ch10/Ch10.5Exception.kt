package ch10

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    supervisorScope {
        launch {
            delay(1000)
            throw Error("some error")
        }

        launch {
            delay(2000)
            println("will be printed")
        }
    }

    coroutineContext.job.children.forEach { it.join() }
    println("done")
}