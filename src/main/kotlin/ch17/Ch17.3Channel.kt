package ch17

import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

suspend fun askMultipleForData3(): String = coroutineScope {
    println("sub -> " + coroutineContext)
    select {
        async { requestData1() }.onAwait { it }
        async { requestData2() }.onAwait { it }
    }.also { coroutineContext.cancelChildren() }
}

suspend fun main(): Unit = coroutineScope {
    println("main -> " + coroutineContext)
    println(askMultipleForData3())
}

/*
출력

//1초후
Data1

// 종료
 */