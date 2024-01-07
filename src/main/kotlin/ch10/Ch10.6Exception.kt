package ch10

import kotlinx.coroutines.*

class MyException: Throwable()
suspend fun main(): Unit = supervisorScope {
    val str1 = async<String> {
        delay(1000)
        throw MyException()
    }

    val str2 = async {
        delay(2000)
        "world"
    }

    try {
        println(str1.await())
    } catch(e: Throwable) {
        println(e)
    }

    println(str2.await())

    coroutineContext.job.children.forEach { it.join() }
}