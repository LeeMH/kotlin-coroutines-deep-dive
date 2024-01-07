package ch6

import kotlin.concurrent.thread

/*
앞의 예제와 동일하지만, 쓰레드를 사용한 케이스이다.
코루틴은 쓰레드보다 아주 가볍다.

>출력
Hello,
1초후
World!
World!
World!
 */
fun main() {
    thread(isDaemon = true) {
        Thread.sleep(1000L)
        println("World!")
    }

    thread(isDaemon = true) {
        Thread.sleep(1000L)
        println("World!")
    }

    thread(isDaemon = true) {
        Thread.sleep(1000L)
        println("World!")
    }

    println("Hello,")
    Thread.sleep(2000L)
}