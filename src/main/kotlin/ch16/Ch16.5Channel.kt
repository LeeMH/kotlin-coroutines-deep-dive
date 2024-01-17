package ch16

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {
    val channel = produce(capacity = Channel.RENDEZVOUS) {
        repeat(5) { index ->
            send(index * 2)
            delay(100)
            println("sent")
        }
    }

    delay(1000)

    for (element in channel) {
        println("Consuming $element")
        delay(1000)
    }
}

/*
//출력

// 1초후
sent
Consuming 0

// 1초후
sent
Consuming 2

// 1초후
sent
Consuming 4

// 1초후
sent
Consuming 6

// 1초후
sent
Consuming 8

// 프로그램종료!!
 */