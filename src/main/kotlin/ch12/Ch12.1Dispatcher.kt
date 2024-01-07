package ch12

import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun main(): Unit = coroutineScope {
    repeat(1_000) {
        launch {
            List(1000) { Random.nextLong() }.maxOrNull()

            val threadName = Thread.currentThread().name
            println("Running on thread : $threadName")
        }
    }
}