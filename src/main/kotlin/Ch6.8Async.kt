import kotlinx.coroutines.*

/*
값이 생성되기 전에 await를 호출하면 값이 생성될때까지 블로킹 된다.


출력>
//1초후
Text1
//2초후
Text2
Text3
 */
fun main(): Unit = runBlocking {
    val res1 = GlobalScope.async {
        delay(1000L)
        "Text1"
    }

    val res2 = GlobalScope.async {
        delay(3000L)
        "Text2"
    }

    val res3 = GlobalScope.async {
        delay(2000L)
        "Text3"
    }

    println(res1.await()) // 1초후 출력
    println(res2.await()) // 2초후 출력
    println(res3.await()) // 즉시 출력
}