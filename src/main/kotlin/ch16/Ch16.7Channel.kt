package ch16

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.produceNumbers() = produce {
    repeat(10) {
        delay(100)
        send(it)
    }
}

fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (message in channel) {
        println("Processor #$id received $message")
    }
}
suspend fun main(): Unit = coroutineScope {
    val channel = produceNumbers()
    repeat(3) { id ->
        delay(10)
        launchProcessor(id, channel)
    }
}

/*
//출력

Processor #0 received 0
Processor #1 received 1
Processor #2 received 2
Processor #0 received 3
Processor #1 received 4
Processor #2 received 5
Processor #0 received 6
Processor #1 received 7
Processor #2 received 8
Processor #0 received 9

// 프로그램종료!!
 */