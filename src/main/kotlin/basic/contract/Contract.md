Contract
---
### Contract 是什么？
Contract 是开发者和 Kotlin 编译器之间定的一些契约，目的是为了让编译器变得更聪明一些。

先来看一个例子
```kotlin
data class Message(val title: String)

/**
 * 判断一个消息是否合法
 * 如果是 true，证明 message 一定不是 null
 */
fun Message?.isMessageValid(): Boolean {
    return this != null && this.title.isNotEmpty()
}

fun testMessageIsValid(msg: Message?) {
    //经过 isMessageValid 判断后的 msg 一定不为 null
    if (msg.isMessageValid()) {
        //但是这里编译时，仍会抛出错误
        println(msg.title) //Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type Message?
    }
}
```
你是不是经常在日常开发中吐槽，为什么我都判过 null 了，可空类型的对象还是需要加 `?` 或者 `!!`，代码看着非常不优雅。

Contract 就是为了解决这类问题的。

### Contract 怎么让编译器更聪明？

```kotlin
data class Message(val title: String)

//This marker distinguishes the experimental contract declaration 
//API and is used to opt-in for that feature when declaring contracts of user functions
@ExperimentalContracts
fun Message?.isMessageValid(): Boolean {
    contract {
       returns(true) implies(this@isMessageValid is Message)
    }
    return this != null && this.title.isNotEmpty()
}

@ExperimentalContracts
fun testMessageIsValid(msg: Message?) {
    if (msg.isMessageValid()) {
        //编译通过
        println(msg.title) 
    }
}
```
- isMessageValid 方法上添加 @ExperimentalContracts 后，才能使用 contract {}
- contract {} 通过 DSL 声明的方式告知编译器一些信息
- returns(true) 表示当方法结果返回 true 的时候，执行后面 implies 的操作
- implies(this@isMessageValid is Message) 表示把当前可空类型的 Message 转为不可空
- 调用处同样需要加 @ExperimentalContracts

### Contract 的其他用法

#### CallsInPlace

再看一个例子
```kotlin
fun testRunSafe() {
    val a: Int
    runSafe {
        a = 100 //Captured values initialization is forbidden due to possible reassignment
    }
}

inline fun runSafe(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e : Exception) {
        println(e.message)
    }
}
```
首先，val a 没初始化或被多次赋值都是编译不能通过的  
其次，编译器不能确定 block 是否只被调用了一次，因为这个场景下，不调用或调用多次都是不对的。

所以需要通过 CallsInPlace 来让编译器变得聪明一点

```kotlin
@ExperimentalContracts
fun testRunSafe() {
    val a: Int
   runSafe {
        a = 100
    }
}

@ExperimentalContracts
inline fun runSafe(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    try {
        block.invoke()
    } catch (e : Exception) {
        println(e.message)
    }
}
```
callsInPlace 的两个参数是：
1. 要执行的 lambda
2. 执行的次数
   1. AT_MOST_ONCE，最多一次
   2. AT_LEAST_ONCE，至少一次
   3. EXACTLY_ONCE，就一次
   4. UNKNOWN，未知次数
 

### 参考
- [Contract，开发者和 Kotlin 编译器之间的契约](https://droidyue.com/blog/2019/08/25/kotlin-contract-between-developers-and-the-compiler/)