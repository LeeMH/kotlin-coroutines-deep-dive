package ch16

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val channel = Channel<Int>()

    launch {
        repeat(5) { index ->
            delay(1000)
            println("Producing next one")
            channel.send(index * 2)
        }
    }

    launch {
        for (element in channel) {
            println("Consuming $element")
        }
    }
}

/*
출력

//1초후
Producing next one
Consuming 0
//1초후
Producing next one
Consuming 2
//1초후
Producing next one
Consuming 4
//1초후
Producing next one
Consuming 6
//1초후
Producing next one
Consuming 8

// 대기
 */