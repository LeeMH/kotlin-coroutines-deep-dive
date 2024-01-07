## 8장. 잡과 자식 코루틴 기다리기

### 8.1 Job이란 무엇인가?

* 잡(job)은 수명을 가지고 있으며 취소 가능하다.
* 잡은 인터페이스이긴 하지만, 구체적인 사용법과 상태를 가지고 있다는 점에서 추상 클래스처럼 다룰 수도 있다.

### 8.2 코루틴 빌더는 부모의 잡을 기초로 자신들의 잡을 생성한다.

* launch의 리턴 타입은 Job이다.
* async의 리턴 타입은 Deferred<T>이지만, Deferred 또한 Job 인터페이스를 구현했다.
* Job은 코루틴컨텍스이므로 coroutineContext[Job]으로 접근할 수 있다.
* 아래와 같이 확장함수를 통해 쉽게 접근할 수도 있다.
```kotlin
fun main() = runBlocking {
    println(coroutineContext.job.isActive)
}
```
* Job은 코루틴이 `상속하지 않는 유일한` 코루틴 컨텍스트이다.
* 아래와 같이 새로운 Job을 통해 빌더를 생성하면 부모/자식 관계가 성립하지 않고, 당연히 내용이 출력되지 않는다.(기다리지 않는다.)
```kotlin
fun main(): Unit = runBlocking {
    launch(Job()) {
        delay(1000)
        println("will not be printed")
    }
}
```

### 8.3 자식들 기다리기

* join 메소드를 이용하여 자식들이 마지막상태(Completed or Cancelled)가 될때까지 기다릴 수 있다.
```kotlin
fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1000)
        println("job1")
    }

    val job2 = launch {
        delay(2000)
        println("job2")
    }

    job1.join()
    job2.join()
    println("All jobs are done")
}
```
* 코루틴 컨텍스트의 job을 통해 자식들의 job에 접근할수 있다.
```kotlin
fun main(): Unit = runBlocking {
    launch {
        delay(1000)
        println("job1")
    }

    launch {
        delay(2000)
        println("job2")
    }

    val children = coroutineContext[Job]?.children

    println("Number of children: ${children?.count()}")
    children?.forEach { it.join() }
    println("All jobs are done")
}
```

### 8.4 잡 팩토리 함수

* Job은 Job() 팩토리 함수를 통해 쉽게 생성할 수 있다.
* Job() 팩토리 함수는 Job이 아닌 하위인터페이스인 CompletableJob을 리턴한다.
```kotlin
public fun Job(parent: Job? = null): CompletableJob
```
* Job() 팩토리 함수를 사용해서 join을 호출하면 해당 작업이 완료되지 않았기 때문에 무한대기 한다. 일반적으로 complete를 호출하고 join을 호출한다.
```kotlin
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    val job = Job()

    launch(job) {
        delay(1000)
        println("job1")
    }

    launch(job) {
        delay(2000)
        println("job2")
    }

    //join전에 complete 메소드를 호출해야 한다.
    //job.complete()
    job.join() // 여기서 무한대기 된다.
    println("will not be printed")
}
```
* CompletableJob은 다음 두가지 메소드를 추가하여 Job인터페이스를 확장하였다.
    * complete(): Boolean
        - 잡을 완료 하는데 사용
        - complete를 호출한 잡에서는 새로운 코루틴이 시작될수 없다.

    * completeExceptionally(exception: Throwable): Boolean
        - 인자로 받은 예외로 잡을 완료시킨다.

````kotlin
// complete 예제
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    val job = Job()

    launch(job) {
        repeat(5) { num ->
            delay(200)
            println("job1: $num")
        }
    }

    launch {
        delay(500)
        job.complete()
    }

    job.join()

    // 이미 완료된 잡이기때문에 실행되지 않는다.
    println(job.isActive)

    launch(job) {
        delay(2000)
        println("will not be printed")
    }
}
````

```kotlin
// completeExceptionally 예제
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    val job = Job()

    // job이 실행되다가, 예외가 발생한 시점에 종료가 된다.
    launch(job) {
        repeat(5) { num ->
            delay(200)
            println("job1: $num")
        }
    }

    launch {
        delay(500)
        job.completeExceptionally(Error("some error!!"))
    }

    job.join()

    // 이미 완료된 잡이기때문에 실행되지 않는다.
    println(job.isActive)

    launch(job) {
        delay(2000)
        println("will not be printed")
    }

    println("done")
}
```

### 8.5 부모 잡을 통한 잡생성 및 취소

* 부모 잡을 통해 자식을 생성 가능하다.
* 당연하지만 부모잡이 취소되면 자식 잡도 취소된다.
```kotlin
import kotlinx.coroutines.*

suspend fun main(): Unit = coroutineScope {
    val parentJob = Job()
    val job = Job(parentJob)

    launch(job) {
        delay(1000)
        println("job1!! ")
    }

    launch(job) {
        delay(2000)
        println("job2!! never printed")
    }

    delay(1100)
    parentJob.cancel()

    job.children.forEach {
        println(it)
        it.join()
    }

    println("done")
}
```