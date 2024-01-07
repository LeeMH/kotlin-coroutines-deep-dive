## 9장. 취소

### 8.1 기본적인 취소

* cancel 메소드를 통해 취소할 수 있다.
* cancel 메소드를 호출하면 다음과 같은 효과를 가져올수 있다.
    - 호출한 코루틴은 첫번째 중담점(아래 예제에서 delay)에서 잡을 끝낸다.
    - 잡이 자식을 가지고 있다면, 그들 또한 취소된다. `하지만 부모는 영향을 받지 않는다.`
    - 취소된 잡은 새로운 코루틴의 부모로 사용될 수 없다.
    - 취소된 잡은 상태가 `Cancelling` 상태가 되었다가, `Cancelled` 상태가 된다.

```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        repeat(1_000) { num ->
            delay(200)
            println("num $num")
        }
    }

    delay(1100)
    job.cancel()
    job.join()
    println("Cancelled successfully")
}
```
* cancel() 메소드 호출후 join() 메소드를 호출하지 않으면 경쟁상태가 발생할 수 있다.
* `cancelAndJoin()` 메소드를 사용하면 이러한 실수를 방지할 수 있다.

### 8.2 취소는 어떻게 작동하는가?

* 잡이 취소되면 `Cancelling` 상태가 되고, 첫번째 중단점에서 `CancellationException`을 던진다.
* try catch 를 사용해서 잡을 수도 있지만, 다시 던지는 것이 좋다.
* 취소된 코루틴이 단순히 멈추는 것이 아니라, 내부적으로 `예외를 사용해` 취소되는 것이다.
* 따라서 finally 블럭 안에서 모든것을 정리할 수 있다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            repeat(1_000) { num ->
                delay(100)
                println("num $num")
            }
        } catch (e: CancellationException) {
            println(e)
            throw e
        } finally {
            println("need to close all resources")
        }
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}
```

### 8.3 취소중 코루틴을 한번 더 호출하기

* 취소된 잡은 상태가 `Cancelling` 상태가 된다.
* 다른 코루틴을 실행하는 것은 불가능하고, `시도하면 무시`하게 된다.
* 또한, 취소된 코루틴을 중단하려 하면 `CancellationException`이 발생한다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
  val job = Job()
  launch(job) {
    try {
      delay(2000)
    } finally {
      println("finally")
      launch { // 무시된다.
        println("will not be printed")
      }
      delay(1000) // 예외발생!!
      println("will not be printed")
    }
  }

  delay(1000)
  job.cancelAndJoin()
  println("Cancelled successfully")
}
```
* 코루틴이 취소된 상태에서도 중단함수를 반드시 호출해야 한다면 `withContext(NonCancelable)`을 사용할 수 있다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delay(200)
            println("coroutine finished")
        } finally {
            println("finally")
            withContext(NonCancellable) {
                delay(1000)
                println("will be printed")
            }
        }
    }

    delay(100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}
```

### 8.4 invokeOnCompletion

* 자원을 해제하는데 자주 사용되는 방법은 Job의 `invokeOnCompletion` 메소드를 사용하는 것이다.
* invokeOnCompletion 메소드는 잡이 `Completed` 또는 `Cancelled` 상태가 됐을때 실행될 핸들러를 지정하는 역할을 한다.
* 핸들러의 파라메터는 `정상종료라면 null`이 전달되고, `취소되었다면 CancellationException`이 전달된다. 
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        delay(1000)
        println("coroutine finished")
    }
  
    // 정상적으로 종료되면 null이 전달되고, 취소되면 CancellationException이 전달된다
    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}
```

### 8.5 중단될 수 없는 걸 중단하기

* 취소는 중담점에서 이뤄지기 때문에 중단점이 없으면 취소가 불가능 하다.

#### 8.5.1 yield

* 이러한 경우, 주기적으로 `yield()` 호출하여 취소를 가능하게 할 수 있다.
* `yield()`는 `코루틴을 중단하고 즉시 다시 실행` 합니다. == delay(0)
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { num ->
            // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            // yield()가 없으면 취소되지 않는다.
            yield()
            println("num $num")
        }
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}
```

#### 8.5.2 잡의 상태(isActive)를 확인하기

* 잡의 상태를 isActive로 확인하고 로직에서 active 일때만 동작하도록 구현할 수 있다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        var num = 0
        do { // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            println("num $num")
            num++
        } while (isActive) // 잡이 active 일때만 동작한다.
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}
```

#### 8.5.3 ensureActive() 사용

* `ensureActive()` 함수를 사용하여, active 상태가 아니라면 CancellationException을 던지도록 한다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { num ->
            // 예제를 위한 코드로, 절대로 이렇게 구현하면 안된다.
            Thread.sleep(200)
            // Active 상태가 아니면, CancellationException을 던진다.
            ensureActive()
            println("num $num")
        }
    }

    job.invokeOnCompletion { e: Throwable? ->
        println("job completed with $e")
    }

    delay(1000)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}
```