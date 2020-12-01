package day01

import forLinesIn

fun main() {
    forLinesIn("day01/input.txt") {
        val numbers = it.toList().map { Integer.parseInt(it) }.toList()
        findSumOf2020(numbers, 0)?.let {
            println("Part 1: ${it.first} + ${it.second} = 2020, " +
                    "${it.first} * ${it.second} = ${it.first * it.second}")
        }

        for (i in numbers.indices) {
            val cur1 = numbers[i]
            findSumOf2020(numbers.subList(i, numbers.size), cur1)?.let {
                println("Part 2: $cur1 + ${it.first} + ${it.second} = 2020, " +
                        "$cur1 * ${it.first} * ${it.second} = ${cur1 * it.first * it.second}")
            }
        }
    }
}

fun findSumOf2020(numbers: List<Int>, additional: Int) : Pair<Int, Int>? {
    for (i in numbers.indices) {
        val cur = numbers[i]
        numbers.subList(i, numbers.size)
            .find { it == 2020 - cur - additional}
            ?.let { return Pair(cur, it) }
    }
    return null
}


