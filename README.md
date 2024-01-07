# kotlin-coroutines-deep-dive

## 6장. 코루틴 빌더

* 모든 중단 함수는 또 다른 중단 함수에 호출 되어야 한다.
* 중단 함수는 시작되는 지점이 반드시 있다.
* `코루틴 빌더`가 그 역할을 한다.
    + launch
    + runBlocking
    + async

### 6.1. launch

### 6.2 runBlocking

### 6.3 async

### 6.4 구조화된 동시성

* 부모는 자식들을 위한 스코프를 제공하고, 자식들은 해당 스코프 내에서  호출된다.
* 이를 통해 `구조화된 동시성`이라는 관계가 성립한다.
* 구조화된 동시성의 특징
    * 자식은 부모로 부터 컨텍스트를 상속 받는다.
    * 부모는 자식이 모두 완료될 때까지 기다린다.
    * 부모 코루틴이 취소되면 자식 코루틴도 모두 취소된다.
    * 자식 코루틴에서 에러가 발생하면 부모 코루틴도 에러로 소멸된다. 

### 6.5 coroutineScope 사용하기

* coroutineScope는 중단 함수 내에서 스코프가 필요할때 일반적으로 사용하는 함수이다.

## 7장. 코루틴 컨텍스트

* 코루틴 빌더의 정의를 보면 첫번째 파라메터는 `CoroutineContext`이다.
```kotlin
fun CoroutineScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job
```
* 마지막 파라메터는 CoroutineScope 타입이고, CoroutineContext를 감싸는 wrapper 이다.
```kotlin
public interface CoroutineScope {
    public val coroutineContext: CoroutineContext
}
```
* Continuation 역시 CoroutineContext를 감싸는 wrapper 이다.
```kotlin
public interface Continuation<in T> {
    public val context: CoroutineContext
    public fun resumeWith(result: Result<T>)
}
```

### 7.1 CoroutineContext 인터페이스

* CorutineContext는 원소나 원소들의 집합을 나타내는 인터페이스이다.
* Job, CoroutineName, CoroutineDispatcher와 같은 Element 객체들이 인덱싱된 집합이라는 점에서 `Map이나 Set`과 같은 컬렉션 개념과 비슷하다.
* 특이한점은 각 Element 또한 CoroutineContext라는 점이다.

### 7.2 CoroutineContext에서 원소 찾기

* get 메소드를 사용하거나, Map처럼 [] 연산자를 사용하여 원소를 찾을 수 있다.
```kotlin
fun main()  {
    val ctx: CoroutineContext = CoroutineName("name1")

    val coroutineName: CoroutineName? = ctx[CoroutineName]
    println(coroutineName?.name)
    val job: Job? = ctx[Job]
    println(job)
}

// 출력
name1
null
```

### 7.3 코루틴 컨텍스트와 빌더

* 자식은 부모로부터 컨텍스트를 상속받는다.

* 모든 자식은 빌더의 인자에서 정의된 특정 컨텍스트를 가질수 있다. 인자로 전달된 컨텍스트는 상속받은 내용을 대체한다.


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