package ch16

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce

suspend fun sendString(
    channel: SendChannel<String>,
    text: String,
    time: Long
) {
    while(true) {
        delay(time)
        channel.send(text)
    }
}
suspend fun main() = runBlocking {
    val channel = Channel<String>()
    launch { sendString(channel, "foo", 200L) }
    launch { sendString(channel, "BAR", 200L) }
    repeat(50) {
        println(channel.receive())
    }

    coroutineContext.cancelChildren()
}

/*
//출력

foo
BAR
foo
BAR
foo
BAR
foo
// 중간생략
BAR
foo
BAR
foo
BAR
foo
BAR

// 프로그램종료!!
 */