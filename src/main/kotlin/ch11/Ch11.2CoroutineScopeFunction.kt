package ch11

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    println("before")

    supervisorScope {
        // 1초후 예외 발생
        launch {
            delay(1000)
            throw Error("some error")
        }

        // 1초후 Hello 2 출력
        launch {
            delay(2000)
            println("Hello 2")
        }
    }

    println("after")
}