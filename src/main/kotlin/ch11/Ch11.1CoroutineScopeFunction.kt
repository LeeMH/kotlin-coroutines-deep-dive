package ch11

import kotlinx.coroutines.*
import log

suspend fun main(): Unit = runBlocking(CoroutineName("main")) {
    log("before")

    // 1초후 출력
    withContext(CoroutineName("C1")) {
        delay(1000)
        log("Hello 1")
    }

    // 1초후 출력
    withContext(CoroutineName("C2")) {
        delay(1000)
        log("Hello 2")
    }

    log("after")
}