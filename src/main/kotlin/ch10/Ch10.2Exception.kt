package ch10

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    // 코루틴 빌더를 try/catch 로 감싸도 무시된다.
    try {
        launch {
            delay(1000)
            throw Error("some error")
        }
    } catch(e: Throwable) {
        println("will not be printed")
    }

    launch {
        delay(2000)
        println("will not be printed")
    }
}