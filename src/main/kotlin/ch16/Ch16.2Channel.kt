package ch16

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {
    val channel = produce {
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

// 프로그램종료!!
 */