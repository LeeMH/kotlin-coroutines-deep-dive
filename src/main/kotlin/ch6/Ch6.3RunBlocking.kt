package ch6

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
runBlocking은 코루틴을 종료할때까지 블로킹 한다.

출력>
//1초후
World!
//1초후
World!
//1초후
World!
Hello,
 */
fun main() {
    runBlocking {
        delay(1000L)
        println("World!")
    }

    runBlocking {
        delay(1000L)
        println("World!")
    }

    runBlocking {
        delay(1000L)
        println("World!")
    }

    println("Hello,")
}