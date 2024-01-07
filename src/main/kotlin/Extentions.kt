import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.log(msg: String) {
    val name = coroutineContext[CoroutineName]?.name
    println("[$name] $msg")
}