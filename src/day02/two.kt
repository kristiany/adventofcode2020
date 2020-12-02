package day02

import forLinesIn

fun main() {
    forLinesIn("day02/input.txt") {
        val inputs = it.map { line ->
            val onDash = line.split("-")
            val onSpace = onDash.get(1).split(" ")
            Entry(onDash.get(0).toInt(),
                    onSpace.get(0).toInt(),
                    onSpace.get(1).split(":").get(0).toCharArray()[0],
                    line.split(": ").get(1))
        }.toList()

        println("Part 1: ${inputs.map { valid(it) }.count { it }} valid passwords")
        println("Part 2: ${inputs.map { xorIndexValid(it) }.count { it }} valid passwords")
    }
}

fun valid(e: Entry): Boolean {
    val count = e.password.count { it == e.char }
    return e.lower <= count && count <= e.upper
}

fun xorIndexValid(e: Entry): Boolean {
    val first = e.password[e.lower - 1]
    val second = e.password[e.upper - 1]
    return first == e.char && second != e.char
            || first != e.char && second == e.char
}

data class Entry(val lower: Int, val upper: Int, val char: Char, val password: String)