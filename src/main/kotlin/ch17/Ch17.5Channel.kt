package ch17

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

fun main(): Unit = runBlocking {
    val c1 = Channel<Char>(capacity = 2)
    val c2 = Channel<Char>(capacity = 2)

    launch {
        for (c in 'A' .. 'H') {
            delay(400)
            select<Unit> {
                c1.onSend(c) {
                    println("From c1 Channel $c")
                }
                c2.onSend(c) {
                    println("From c2 channel $c")
                }
            }
        }
    }

    launch {
        while(true) {
            delay(1000)
            val c = select {
                c1.onReceive { "$it from 1"}
                c2.onReceive { "$it from 2"}
            }

            println("Received $c")
        }
    }
}
/*
출력

From c1 Channel A
From c1 Channel B
Received A from 1
From c1 Channel C
From c2 channel D
Received B from 1
From c1 Channel E
From c2 channel F
Received C from 1
From c1 Channel G
Received E from 1
From c1 Channel H
Received G from 1
Received H from 1
Received D from 2
Received F from 2

// 종료
 */