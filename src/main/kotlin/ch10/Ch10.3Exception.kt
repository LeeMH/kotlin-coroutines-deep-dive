package ch10

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val scope = CoroutineScope(SupervisorJob())
    scope.launch {
        delay(1000)
        throw Error("some error")
    }

    scope.launch {
        delay(2000)
        println("will be printed")
    }

    scope.coroutineContext.job.children.forEach { it.join() }
}