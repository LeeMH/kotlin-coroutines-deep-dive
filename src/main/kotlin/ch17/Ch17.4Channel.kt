package ch17

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun CoroutineScope.produceString(
    s: String,
    time: Long
) = produce {
    while (true) {
        delay(time)
        send(s)
    }
}

fun main() = runBlocking {
    val channel1 = produceString("foo", 210L)
    val channel2 = produceString("BAR", 500L)

    repeat(7) {
        select<Unit> {
            channel1.onReceive { data ->
                println("From foo Channel $data")
            }
            channel2.onReceive { data ->
                println("From BAR channel $data")
            }
        }
    }

    coroutineContext.cancelChildren()
}
/*
출력

From foo Channel foo
From foo Channel foo
From BAR channel BAR
From foo Channel foo
From foo Channel foo
From BAR channel BAR
From foo Channel foo

// 종료
 */