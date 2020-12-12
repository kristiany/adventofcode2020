package day11

import forLinesIn

fun main() {
    forLinesIn("day11/input.txt") {
        val input = it.toList()
        val w = input[0].length
        val h = input.size
        val map = input.joinToString("")
        //println("Init")
        //printMap(map, w)
        val result1 = run(map, w, h, 4, ::adjacents)
        println("Part 1: occupied seats ${result1.filter { it == '#' }.count()}")

        val result2 = run(map, w, h, 5, ::adjacentsFirstSee)
        println("Part 2: occupied seats ${result2.filter { it == '#' }.count()}")
    }
}

fun printMap(map: String, w: Int) {
    for (line in map.chunked(w)) {
        println(line)
    }
}

fun run(map: String, w: Int, h: Int, atLeastOccupied: Int, f: (String, Int, Int, Int) -> List<Char>): String {
    var workMap = map
    for (x in 1..200) {
        //println("Round $x")

        val result = StringBuilder()
        var i = 0
        var anyChanges = false
        for (c in workMap) {
            if (c == 'L' && noOccupiedAdjacents(workMap, w, h, i, f)) {
                result.append("#")
                anyChanges = true
            } else if (c == '#' && atLeastOccupiedAdjacents(workMap, w, h, atLeastOccupied, i, f)) {
                result.append("L")
                anyChanges = true
            } else {
                result.append(c)
            }
            ++i
        }
        workMap = result.toString()
        if (!anyChanges) {
            println("No changes at round $x")
            return workMap
        }
        //printMap(workMap, w)
    }
    return workMap
}

fun noOccupiedAdjacents(map: String, w: Int, h: Int, index: Int, f: (String, Int, Int, Int) -> List<Char>): Boolean {
    val adjs = f(map, w, h, index)
    return adjs.all { it != '#' }
}

fun atLeastOccupiedAdjacents(map: String, w: Int, h: Int, atLeastOccupied: Int, index: Int, f: (String, Int, Int, Int) -> List<Char>): Boolean {
    val adjs = f(map, w, h, index)
    return adjs.filter { it == '#' }.count() >= atLeastOccupied
}

fun adjacents(map: String, w: Int, h: Int, index: Int): List<Char> {
    val result = ArrayList<Char>()
    val size = w * h

    // Above left
    if (index - w > 0 && index % w > 0) {
        result.add(map[index - w - 1])
    }
    // Above
    if (index - w >= 0) {
        result.add(map[index - w])
    }
    // Above right
    if (index - w >= 0 && index % w < w - 1) {
        result.add(map[index - w + 1])
    }
    // Right
    if (index < size - 1 && index % w < w - 1) {
        result.add(map[index + 1])
    }
    // Below right
    if (index + w + 1 < size && index % w < w - 1) {
        result.add(map[index + w + 1])
    }
    // Below
    if (index + w < size) {
        result.add(map[index + w])
    }
    // Below left
    if (index + w < size && index % w > 0) {
        result.add(map[index + w - 1])
    }
    // Left
    if (index > 0 && index % w > 0) {
        result.add(map[index - 1])
    }
    return result
}

fun adjacentsFirstSee(map: String, w: Int, h: Int, index: Int): List<Char> {
    val result = ArrayList<Char>()
    val size = w * h

    // Above left
    var i = index - w - 1
    while (i >= 0 && i % w >= 0 && i % w < index % w && map[i] == '.') {
        i = i - w - 1
    }
    if (i >= 0 && i % w >= 0 && i % w < index % w && map[i] != '.') {
        result.add(map[i])
    }
    // Above
    i = index - w
    while (i >= 0 && map[i] == '.') {
        i -= w
    }
    if (i >= 0 && map[i] != '.') {
        result.add(map[i])
    }
    // Above right
    i = index - w + 1
    while (i >= 0 && i % w <= w - 1 && i % w > index % w && map[i] == '.') {
        i = i - w + 1
    }
    if (i >= 0 && i % w <= w - 1 && i % w > index % w && map[i] != '.') {
        result.add(map[i])
    }
    // Right
    i = index + 1
    while (i < size && i % w <= w - 1 && i % w > index % w && map[i] == '.') {
        i += 1
    }
    if (i < size && i % w <= w - 1 && i % w > index % w && map[i] != '.') {
        result.add(map[i])
    }
    // Below right
    i = index + w + 1
    while (i < size && i % w <= w - 1 && i % w > index % w && map[i] == '.') {
        i += w + 1
    }
    if (i < size && i % w <= w - 1 && i % w > index % w && map[i] != '.') {
        result.add(map[i])
    }
    // Below
    i = index + w
    while (i < size && map[i] == '.') {
        i += w
    }
    if (i < size && map[i] != '.') {
        result.add(map[i])
    }
    // Below left
    i = index + w - 1
    while (i < size && i % w >= 0 && i % w < index % w && map[i] == '.') {
        i = i + w - 1
    }
    if (i < size && i % w >= 0 && i % w < index % w && map[i] != '.') {
        result.add(map[i])
    }
    // Left
    i = index - 1
    while (i >= 0 && i % w >= 0 && i % w < index % w && map[i] == '.') {
        i -= 1
    }
    if (i >= 0 && i % w >= 0 && i % w < index % w && map[i] != '.') {
        result.add(map[i])
    }
    return result
}