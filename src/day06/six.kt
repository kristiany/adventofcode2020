package day06

import forLinesIn

fun main() {
    forLinesIn("day06/input.txt") {
        val (anyone, everyone) = count(it)
        println("Part 1: sum $anyone")
        println("Part 2: sum $everyone")
    }
}

private fun count(input: Sequence<String>): Pair<Int, Int> {
    val groups = ArrayList<List<Set<Char>>>()
    var group = ArrayList<Set<Char>>()
    for (line in input) {
        if (line.isBlank()) {
            groups.add(group)
            group = ArrayList()
        } else {
            group.add(HashSet(line.trim().toCharArray().asList()))
        }
    }
    groups.add(group)
    return Pair(
        groups.map { g -> g.reduce { acc, s -> acc.union(s) }.size }.sum(),
        groups.map { g -> g.reduce { acc, s -> acc.intersect(s) }.size }.sum())
}
