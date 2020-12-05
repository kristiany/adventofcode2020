package day05

import forLinesIn

fun main() {
    val bpTest1 = "BBFFBBFRLL"
    println("Test: seat id ${row(bpTest1) * 8 + seat(bpTest1)}")

    forLinesIn("day05/input.txt") {

        val seatIds = it.map { bp -> row(bp) * 8 + seat(bp) }.toSet()

        val min = seatIds.minOrNull()!!
        val max = seatIds.maxOrNull()!!

        val mySeat = (min..max).toSet().minus(seatIds).first()

        println("Part 1: highest $max")
        println("Part 2: my seat $mySeat")
    }

}

fun seat(bp: String): Int {
    val code = bp.replace(Regex("[FB]+"), "");
    return find(code, 0..7, 'L')
}

fun row(bp: String): Int {
    val code = bp.replace(Regex("[LR]+"), "");
    return find(code, 0..127, 'F')
}

private fun find(code: String, startRange: IntRange, firstCodeChar: Char): Int {
    var r = startRange
    for (c in code) {
        val diff = (r.last - r.first + 1) / 2
        r = if (c == firstCodeChar) {
            r.first..r.last - diff
        } else {
            r.first + diff..r.last
        }
        //println("$r1")
    }
    return r.first
}
