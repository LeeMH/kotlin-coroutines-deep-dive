package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { num ->
            // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            // Active 상태가 아니면, CancellationException을 던진다.
            ensureActive()
            println("num $num")
        }
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}