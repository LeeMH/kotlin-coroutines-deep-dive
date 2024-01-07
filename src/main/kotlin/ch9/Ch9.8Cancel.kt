package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        var num = 0
        do { // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            println("num $num")
            num++
        } while (isActive)
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}