package ch10

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    launch {
        launch {
            delay(1000)
            throw Error("some error")
        }

        launch {
            delay(2000)
            println("will not be printed")
        }

        launch {
            delay(500)
            println("will be printed")
        }
    }

    launch {
        delay(2000)
        println("will not be printed")
    }
}