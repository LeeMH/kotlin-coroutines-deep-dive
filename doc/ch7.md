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
