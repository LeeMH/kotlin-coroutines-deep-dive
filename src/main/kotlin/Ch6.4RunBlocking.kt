/*
앞의 예제와 동일한 결과를 얻는 코드이고, 쓰레드를 사용한 예제이다.

출력>
//1초후
World!
//1초후
World!
//1초후
World!
Hello,
 */
fun main() {
    Thread.sleep(1000L)
    println("World!")

    Thread.sleep(1000L)
    println("World!")

    Thread.sleep(1000L)
    println("World!")

    println("Hello,")
}