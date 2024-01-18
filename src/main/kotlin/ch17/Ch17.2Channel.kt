package ch17

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

suspend fun askMultipleForData2(): String = coroutineScope {
    println("sub -> " + coroutineContext)
    select {
        async { requestData1() }.onAwait { it }
        async { requestData2() }.onAwait { it }
    }
}

suspend fun main(): Unit = coroutineScope {
    println("main -> " + coroutineContext)
    println(askMultipleForData2())
}

/*
출력

//10초후
Data1

// 종료
 */