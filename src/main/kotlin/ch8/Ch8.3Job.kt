package ch8

import kotlinx.coroutines.*

/*
join 메소드를 통해 자식들을 기다린다.

출력>
// 1초후
job1
// 1초후
job2
All jobs are done
 */
fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1000)
        println("job1")
    }

    val job2 = launch {
        delay(2000)
        println("job2")
    }

    job1.join()
    job2.join()
    println("All jobs are done")
}