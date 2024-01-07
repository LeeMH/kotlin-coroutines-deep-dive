import kotlinx.coroutines.*


fun main(): Unit = runBlocking {
    val job = Job()

    // job이 실행되다가, 예외가 발생한 시점에 종료가 된다.
    launch(job) {
        repeat(5) { num ->
            delay(200)
            println("job1: $num")
        }
    }

    launch {
        delay(500)
        job.completeExceptionally(Error("some error!!"))
    }

    job.join()

    // 이미 완료된 잡이기때문에 실행되지 않는다.
    println(job.isActive)

    launch(job) {
        delay(2000)
        println("will not be printed")
    }

    println("done")
}