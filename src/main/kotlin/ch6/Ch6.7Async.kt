package ch6

import kotlinx.coroutines.*

/*
async 는 launch와 비슷하지만, 값을 반환한다.
리턴 타입은 Deferred<T> 이다.
Deferred에는 작업이 끝나면 값을 리턴하는 await 메소드가 있다.

출력>
//1초후
42
 */
fun main(): Unit = runBlocking {
    val resultDeferred: Deferred<Int> = GlobalScope.async {
        delay(1000L)
        42
    }

    //다른 작업을 한다.
    val result: Int = resultDeferred.await()
    println(result)
}