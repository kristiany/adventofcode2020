package day13

import forLinesIn

fun main() {
    forLinesIn("day13/input.txt") {
        val input = it.toList()
        val timestamp = input[0].toInt()
        val busses = input[1].trim().split(",").filter { it != "x" }.map { it.toInt() }

        val earliest = busses.map { Pair(it, it - timestamp % it) }.sortedBy { it.second }.first()
        println("Part 1: wait ${earliest.second} min * bus id ${earliest.first} = ${earliest.second * earliest.first}")

        val inputPart2 = input[1]
        //val inputPart2 = "17,x,13,19" // 3417
        //val inputPart2 = "67,7,59,61" // 754018
        //val inputPart2 = "67,x,7,59,61" // 779210
        //val inputPart2 = "67,7,x,59,61" // 1261476
        //val inputPart2 = "1789,37,47,1889" // 1202161486
        var offset = 0
        val departures = ArrayList<Bus>()
        for (i in inputPart2.trim().split(",")) {
            if (i != "x") {
                departures.add(Bus(i.toInt(), offset))
            }
            ++offset
        }

        println(departures)
        val t = findT(departures)
        println("Part 2: t = $t")
    }
}

fun findT(departures: List<Bus>): Any {
    val sorted = departures.sortedByDescending { it.id }
    val base = sorted[0].id
    val baseOffset = sorted[0].offset
    val rebased = sorted.map { Bus(it.id, it.offset - baseOffset) }
    println(rebased)

    var alignmentsFound = 1
    var step = base.toLong()
    var t = base.toLong()
    var firstAlignedT = - 1L
    while (t < Long.MAX_VALUE) {
        val aligns = aligns(t, rebased)
        if (aligns.second) {
            return t - baseOffset
        }

        if (aligns.first > alignmentsFound) {
            if (firstAlignedT >= 0L) {
                alignmentsFound = aligns.first
                step = t - firstAlignedT
                println("Found ${aligns.first} alignments: increasing step to $t - $firstAlignedT = $step")
                firstAlignedT = -1L
            } else {
                println("Found first of ${aligns.first} alignments, storing lower bound $t")
                firstAlignedT = t
            }
        }

        t += step
    }
    return -1
}

fun aligns(t: Long, busses: List<Bus>): Pair<Int, Boolean> {
    var found = 0
    for (b in busses) {
        if ((t + b.offset) % b.id != 0L) {
            return Pair(found, false)
        }
        ++found
    }
    return Pair(found, true)
}

data class Bus(val id: Int, val offset: Int)