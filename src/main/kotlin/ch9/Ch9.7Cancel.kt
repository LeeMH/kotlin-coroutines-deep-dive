package ch9

import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { num ->
            // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            // yield()가 없으면 취소되지 않는다.
            yield()
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