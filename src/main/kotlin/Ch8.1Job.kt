import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
job은 유일하게 상속되지 않는 컨텍스트이다.

>출력
main
true
false
true

 */
fun main(): Unit = runBlocking {
    val name = CoroutineName("main")
    val job = Job()

    launch(name + job) {
        val childName = coroutineContext[CoroutineName]
        println(childName?.name) // main
        println(childName == name) // true
        val childJob = coroutineContext[Job]
        println(childJob == job) // false
        println(childJob == job.children.first()) // true
    }
}