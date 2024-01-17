package ch16

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {
    val channel = produce(capacity = 3) {
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

sent
//0.1초후
sent
//0.1초후
sent

// 약 0.8초후
Consuming 0
sent

// 1초후
Consuming 2
sent

// 1초후
Consuming 4
Consuming 6
Consuming 8

// 프로그램종료!!
 */