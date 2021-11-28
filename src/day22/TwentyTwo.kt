package day22

import forLinesIn

fun main() {
    forLinesIn("day22/input.txt") {
        val (player1, player2) = parse(it.toList())
        println(player1)
        println(player2)

        val winningStack = play(player1, player2)
        val score = toScore(winningStack)
        println("Part 1: $score")

        val winningStack2 = playRecursive(player1, player2)
        val score2 = toScore(winningStack2)
        println("Part 2: $score2")
    }
}

fun playRecursive(player1: List<Int>, player2: List<Int>): ArrayDeque<Int> {
    val stack1 = ArrayDeque(player1)
    val stack2 = ArrayDeque(player2)
    val result = playRecGame(ArrayDeque(stack1), ArrayDeque(stack2), 1)
    return result.second
}

fun playRecGame(stack1: ArrayDeque<Int>, stack2: ArrayDeque<Int>, game: Int): Pair<Int, ArrayDeque<Int>> {

    println("Game $game")
    val stack1Mem = HashSet<List<Int>>()
    val stack2Mem = HashSet<List<Int>>()
    var round = 1
    var gameCounter = game
    while (stack1.isNotEmpty() && stack2.isNotEmpty()) {
        if (stack1Mem.contains(stack1.toList()) && stack2Mem.contains(stack2.toList())) {
            println("Recursion guard, player 1 wins")
            return Pair(1, stack1) // player 1 win
        }
        stack1Mem.add(ArrayList(stack1))
        stack2Mem.add(ArrayList(stack2))
        //println("--- Round $round")
        //println("Player1: $stack1")
        //println("Player2: $stack2")
        val player1Card = stack1.removeFirst()
        val player2Card = stack2.removeFirst()
        if (stack1.size >= player1Card && stack2.size >= player2Card) {
            // New subgame
            gameCounter++
            val result = playRecGame(ArrayDeque(stack1.subList(0, player1Card)),
                ArrayDeque(stack2.subList(0, player2Card)), gameCounter)
            println("Back to game $game")
            if (result.first == 1) {
                println("Player 1 wins subgame, $player1Card, $player2Card!")
                stack1.addLast(player1Card)
                stack1.addLast(player2Card)
            }
            else {
                println("Player 2 wins subgame, $player2Card, $player1Card!")
                stack2.addLast(player2Card)
                stack2.addLast(player1Card)
            }
        }
        else {
            addWin(player1Card, player2Card, stack1, stack2)
        }
        round++
    }

    return if (stack1.isEmpty()) Pair(2, stack2) else Pair(1, stack1)
}

private fun play(player1: List<Int>, player2: List<Int>): ArrayDeque<Int> {
    val stack1 = ArrayDeque(player1)
    val stack2 = ArrayDeque(player2)
    var round = 1
    while (stack1.isNotEmpty() && stack2.isNotEmpty()) {
        //println("--- Round $round")
        //println("Player1: $stack1")
        //println("Player2: $stack2")
        val player1Card = stack1.removeFirst()
        val player2Card = stack2.removeFirst()
        addWin(player1Card, player2Card, stack1, stack2)

        round++
    }
    return if (stack1.isEmpty()) stack2 else stack1
}

private fun addWin(
    player1Card: Int,
    player2Card: Int,
    stack1: ArrayDeque<Int>,
    stack2: ArrayDeque<Int>
) {
    if (player1Card > player2Card) {
        println("Player 1 wins with $player1Card over $player2Card!")
        stack1.addLast(player1Card)
        stack1.addLast(player2Card)
    } else if (player2Card > player1Card) {
        println("Player 2 wins with $player2Card over $player1Card!")
        stack2.addLast(player2Card)
        stack2.addLast(player1Card)
    } else {
        println("A draw!!!")
    }
}

private fun toScore(stack: ArrayDeque<Int>): Int {
    println(stack)
    return stack.reversed()
        .zip(1..stack.size)
        .map { it.first * it.second }
        .sum()
}

fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
    val player2InputIndex = input.indexOf("Player 2:")
    val player1Cards = input.subList(1, player2InputIndex)
        .filter { ! it.isBlank() }
        .map { it.toInt() }
        .toList()
    val player2Cards = input.subList(player2InputIndex + 1, input.size)
        .filter { ! it.isBlank() }
        .map { it.toInt() }
        .toList()
    return Pair(player1Cards, player2Cards)
}
