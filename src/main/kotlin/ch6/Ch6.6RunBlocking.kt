package ch6

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    println("Hello,")
    delay(2000L)
}