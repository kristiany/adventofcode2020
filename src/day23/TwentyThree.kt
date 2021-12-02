package day23

import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    //val input = "389125467"  // Test
    val input = "916438275"  // Input
    var cups = ArrayDeque(input.map { it.digitToInt() }.toList())
    for (i in 1..100) {
        cups = move(i, cups, 9)
    }
    val labelOneIndex = cups.indexOf(1)
    println("Part 1: ${ rotateFwd(labelOneIndex, cups).subList(1, cups.size).joinToString("") }")

    val cups2 = LinkedList(input.map { it.digitToInt() }.toList())
    val linkedCups = HashMap(cups2.windowed(2).associate { it[0] to it[1] })
    (10..999_999).forEach { linkedCups[it] = it + 1 }
    val labelSize = 1000_000
    linkedCups[labelSize] = cups2[0]
    linkedCups[cups2[8]] = 10 // connect original last with the additional first
    var currentLabel = cups2[0]
    for (i in 1..10_000_000) {
        currentLabel = move2(i, currentLabel, linkedCups, labelSize)
    }
    println("Part 2: ${linkedCups[1]!!.toLong() * linkedCups[linkedCups[1]]!!.toLong()}")
}

fun move2(move: Int,
          currentLabel: Int,
          cups: HashMap<Int, Int>,
          labelSize: Int): Int {
    val three = next3(currentLabel, cups)

    if (move % 1000000 == 0) {
        println("move $move")
    }
    cups[currentLabel] = cups[three[2]]!!
    val dest = findDestLabel(currentLabel, three.toSet(), labelSize)
    insertAfter2(dest, three, cups)
    return cups[currentLabel]!!
}

fun next3(currentLabel: Int, cups: HashMap<Int, Int>): List<Int> {
    val result = ArrayList<Int>()
    var labelIt = currentLabel
    for (i in 1..3) {
        labelIt = cups[labelIt]!!
        result.add(labelIt)
    }
    return result
}

private fun move(
    i: Int,
    cupsInput: ArrayDeque<Int>,
    labelSize: Int
): ArrayDeque<Int> {
    var cups = ArrayDeque(cupsInput)
    val three = cups.subList(1, 4)
    val dest = findDestLabel(cups.first(), three.toSet(), labelSize)
    val remains = cups.minus(three)
    cups = insertAfter(dest, three, remains)
    cups = rotateFwd(1, cups)
    return cups
}

fun rotateFwd(steps: Int, arr: ArrayDeque<Int>): ArrayDeque<Int> {
    val copy = ArrayDeque(arr)
    (1..steps).forEach {
        copy.addLast(copy.removeFirst())
    }
    return copy
}

fun insertAfter(label: Int, toInsert: List<Int>, arr: List<Int>): ArrayDeque<Int> {
    val index = arr.indexOf(label)
    val copy = ArrayDeque(arr)
    copy.addAll(index + 1, toInsert)
    return copy
}

fun insertAfter2(label: Int, toInsert: List<Int>, cups: HashMap<Int, Int>) {
    val connected = cups[label]!!
    cups[label] = toInsert[0]
    cups[toInsert[0]] = toInsert[1]
    cups[toInsert[1]] = toInsert[2]
    cups[toInsert[2]] = connected
}

fun findDestLabel(currentLabel: Int, three: Set<Int>, labelSize: Int): Int {
    var dest = if (currentLabel < 2) labelSize else currentLabel - 1
    while (three.contains(dest)) {
        dest = if (dest < 2) labelSize else dest - 1
    }
    return dest
}
