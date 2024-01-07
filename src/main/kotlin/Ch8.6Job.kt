import kotlinx.coroutines.*


fun main(): Unit = runBlocking {
    val job = Job()

    launch(job) {
        repeat(5) { num ->
            delay(200)
            println("job1: $num")
        }
    }

    launch {
        delay(500)
        job.complete()
    }

    job.join()

    // 이미 완료된 잡이기때문에 실행되지 않는다.
    println(job.isActive)

    launch(job) {
        delay(2000)
        println("will not be printed")
    }
}