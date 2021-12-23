package basic.sequence

fun main() {
//    list()
//    println("==============================================")
    sequence()
}

fun list() {
    val words = "The quick brown fox jumps over the lazy dog".split(" ")
    val wordList = words.toList()
    val lengthList = wordList.filter{ println("filter $it"); it.length > 3}
        .map { println("map ${it.length}"); it.length }
        .take(4)
    println("Lengths of first 4 words longer than 3 chars")
    println(lengthList)
}

fun sequence() {
    val words = "The quick brown fox jumps over the lazy dog".split(" ")
    val wordSeq = words.asSequence()
    val lengthSeq = wordSeq
        .filter {
            println("filter $it")
            it.length > 3
        }
        .map {
            println("map ${it.length}")
            it.length
        }
        .take(4)

    println("Lengths of first 4 words longer than 3 chars")
    println(lengthSeq.toList())
}

fun sequenceBlock() {
    sequence<Int> {
        yield(1)
    }
}


