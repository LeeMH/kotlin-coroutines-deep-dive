import kotlinx.coroutines.*

/*
새로운 Job을 넣으면 부모와 연관성이 없어지기 때문에, 작업을 기다리지 않는다.
 */
fun main(): Unit = runBlocking {
    launch(Job()) {
        delay(1000)
        println("will not be printed")
    }
}