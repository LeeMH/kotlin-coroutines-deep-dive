import kotlinx.coroutines.*

/*
코루틴 컨텍스트의 job을 통해 자식 코루틴에 접근할수 있다.

출력>
// 1초후
job1
// 1초후
job2
All jobs are done
 */
fun main(): Unit = runBlocking {
    launch {
        delay(1000)
        println("job1")
    }

    launch {
        delay(2000)
        println("job2")
    }

    val children = coroutineContext[Job]?.children

    println("Number of children: ${children?.count()}")
    children?.forEach { it.join() }
    println("All jobs are done")
}