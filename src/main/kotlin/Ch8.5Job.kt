import kotlinx.coroutines.*

/*
Job() 팩토리 함수를 사용하면 코루틴 없이도 Job을 만들수 있다.
흔한 실수는 아래와 깉이 job.join()을 호출하는것이다.
자식잡과는 별개로 해당 job자체가 여전히 active이기 때문에 무한대기된다.
 */
fun main(): Unit = runBlocking {
    val job = Job()

    launch(job) {
        delay(1000)
        println("job1")
    }

    launch(job) {
        delay(2000)
        println("job2")
    }

    //join전에 complete 메소드를 호출해야 한다.
    //job.complete()
    job.join() // 여기서 무한대기 된다.
    println("will not be printed")
}