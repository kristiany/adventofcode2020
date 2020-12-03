package day03

import forLinesIn

fun main() {
    forLinesIn("day03/input.txt") {
        val map = it.map { line -> line.trim() }.toList();

        val treesR3D1 = treesInSlope(map, 3)
        println("Part 1: $treesR3D1 trees")

        val treesR1D1 = treesInSlope(map, 1)
        val treesR5D1 = treesInSlope(map, 5)
        val treesR7D1 = treesInSlope(map, 7)
        val treesR1D2 = treesInSlope(map, 1, 2)

        println("Part 2: $treesR1D1 * $treesR3D1 * $treesR5D1 * $treesR7D1 * $treesR1D2 = " +
                "${treesR1D1 * treesR3D1 * treesR5D1 * treesR7D1 * treesR1D2}")
    }
}

fun treesInSlope(map: List<String>, stepRight: Int, stepDown: Int = 1): Long {
    val sectionWidth = map[0].length
    var trees = 0L
    var steps = 1
    for (y in stepDown until map.size step stepDown) {
        if (map[y][(steps * stepRight) % sectionWidth] == '#') {
            ++trees
        }
        ++steps
    }
    return trees
}