import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
각각의 코루틴들이 동시에 실행되는 것을 확인할수 있다.

>출력
Hello,
1초후
World!
World!
World!
 */
fun main() {
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
    Thread.sleep(2000L)
}