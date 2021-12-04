package day25

fun main() {

    // input
    val keya = 1327981L
    val keyb = 2822615L

    // test
    //val keya = 17807724L
    //val keyb = 5764801L

    val loopsizea = findLoopSize(keya)
    val loopsizeb = findLoopSize(keyb)
    println("Loopsize a $loopsizea")
    println("Loopsize b $loopsizeb")
    val encKey1 = findEncKey(keya, loopsizeb)
    val encKey2 = findEncKey(keyb, loopsizea)
    println("Part 1: enc keys $encKey1, $encKey2")
}

fun findEncKey(publicKey: Long, loopSize: Long): Long {
    var value = 1L
    for (l in 1 .. loopSize) {
        value *= publicKey
        value %= 20201227L
    }
    return value
}

fun findLoopSize(publicKey: Long): Long {
    var loopSize = 0L
    var value = 1L
    while (value != publicKey) {
        value *= 7L
        value %= 20201227L
        loopSize++
    }
    return loopSize
}