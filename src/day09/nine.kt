package day09

import forLinesIn

fun main() {
    forLinesIn("day09/input.txt") {
        var input = it.map { it.toLong() }.toList()

        var preambleSize = 25
        var invalid = 0L
        for (i in preambleSize until input.size) {
            if (!sumOf(input.subList(i - preambleSize, i), input[i])) {
                invalid = input[i]
                break
            }
        }
        println("Part 1: not a sum of preamble $invalid")

        for (i in input.indices) {
            var sum = input[i]
            var j = i + 1
            while (sum < invalid && j < input.size) {
                sum += input[j]
                ++j
            }
            if (sum == invalid) {
                val set = input.subList(i, j)
                println("Part 2: weekness ${set.maxOrNull()!! + set.minOrNull()!!}")
                break
            }
        }

    }
}

fun sumOf(preamble: List<Long>, target: Long): Boolean {
    for (i in preamble.indices) {
        for (j in i + 1 until preamble.size) {
            if (preamble[i] + preamble[j] == target) {
                return true
            }
        }
    }
    return false;
}