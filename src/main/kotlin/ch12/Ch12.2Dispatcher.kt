@file:OptIn(ExperimentalCoroutinesApi::class)

package ch12

import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun main(): Unit = coroutineScope {
    val dispatcher = Dispatchers.Default.limitedParallelism(3)
    repeat(1_000) {
        launch(dispatcher) {
            List(1000) { Random.nextLong() }.maxOrNull()

            val threadName = Thread.currentThread().name
            println("Running on thread : $threadName")
        }
    }
}