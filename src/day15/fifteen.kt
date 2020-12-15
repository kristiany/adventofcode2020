package day15


fun main() {
    //val input = listOf(0, 3, 6) // 436
    //val input = listOf(1, 3, 2) // 1
    //val input = listOf(2, 1, 3) // 10
    //val input = listOf(1, 2, 3) // 27
    //val input = listOf(2, 3, 1) // 78
    //val input = listOf(3, 2, 1) // 438
    //val input = listOf(3, 1, 2) // 1836
    val input = listOf(1,0,15,2,10,13)  // PROD

    val result1 = play(input)
    println("Part 1: 2020th spoken is $result1")

    val result2 = play(input, 30000000)
    println("Part 2: 30000000th spoken is $result2")
}

private fun play(input: List<Int>, maxTurns: Int = 2020): Int {
    val board = HashMap<Int, IntArray>()
    var turn = 1
    for (nr in input) {
        board[nr] = intArrayOf(turn)
        ++turn
    }

    var isNew = true
    var current = input.last()
    for (t in turn..maxTurns) {
        if (isNew) {
            current = 0
        } else {
            board[current]?.let {
                if (it.size == 1) {
                    current = t - it.first()
                } else {
                    current = it.last() - it[it.lastIndex - 1]
                }
            }
        }
        if (board.containsKey(current)) {
            // Clear memory of old not needed entries, only keep last two
            val recent = board[current]!!.last()
            board[current] = intArrayOf(recent, t)
            isNew = false
        } else {
            board[current] = intArrayOf(t)
            isNew = true
        }
        if (t % 1000000 == 0) {
            println("t: $t")
        }
    }
    return current
}
