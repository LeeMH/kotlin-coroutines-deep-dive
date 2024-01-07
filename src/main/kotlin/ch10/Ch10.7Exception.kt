package ch10

import kotlinx.coroutines.*

class MyNonPropagationException: CancellationException()
suspend fun main(): Unit = coroutineScope {
    launch {
        launch {
            delay(1000)
            println("will not be printed")
        }

        throw MyNonPropagationException()
    }

    launch {
        delay(2000)
        println("will be printed")
    }

    coroutineContext.job.children.forEach { it.join() }
    println("done")
}