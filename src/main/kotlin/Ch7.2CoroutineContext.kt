import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

fun main()  {
    val ctx: CoroutineContext = CoroutineName("name1")

    val coroutineName: CoroutineName? = ctx[CoroutineName]
    println(coroutineName?.name)
    val job: Job? = ctx[Job]
    println(job)
}