import kotlinx.coroutines.*

/*
부모의 컨텍스트는 자식에게 상속된다.

>출력
[main] started
//1초후
[main] Running launch
//1초후
[main] Running async
[main] The answer is 42
 */
fun main(): Unit = runBlocking(CoroutineName("main")) {
    log("started")

    val v1 = async {
        delay(2000)
        log("Running async")
        42
    }

    launch {
        delay(1000)
        log("Running launch")
    }

    log("The answer is ${v1.await()}")
}