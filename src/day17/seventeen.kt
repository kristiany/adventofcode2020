package day17

import forLinesIn
import kotlin.collections.HashMap

fun main() {
    forLinesIn("day17/input.txt") {
        val space = HashMap<Coord, Boolean>()
        val input = it.toList()
        val height = input.size
        val width = input[0].length
        for (y in input.indices) {
            for (x in input[y].trim().indices) {
                space.put(Coord(x, y, 0), input[y][x] == '#')
            }
        }
        println(space)
        var space3d = space
        var searchSpace = SearchSpace(Coord(-1, -1, -1), Coord(width + 1, height + 1, 1))
        for (i in 1..6) {
            println("Cycle $i")
            space3d = cycle(space3d, searchSpace)
            //debug3d(space3d)
            searchSpace = searchSpace.grow3d()
        }
        println("Part 1: active ${space3d.values.count { it }}")

        var space4d = space
        var searchSpace4d = SearchSpace(Coord(-1, -1, -1, -1), Coord(width + 1, height + 1, 1, 1))
        for (i in 1..6) {
            println("Cycle $i")
            space4d = cycle(space4d, searchSpace4d, true)
            searchSpace4d = searchSpace4d.grow4d()
        }
        println("Part 2: active ${space4d.values.count { it }}")
    }
}

fun cycle(space: Map<Coord, Boolean>, searchSpace: SearchSpace, includeW: Boolean = false): HashMap<Coord, Boolean> {
    val newSpace = HashMap<Coord, Boolean>()
    for (w in searchSpace.a.w..searchSpace.b.w) {
        for (z in searchSpace.a.z..searchSpace.b.z) {
            for (y in searchSpace.a.y..searchSpace.b.y) {
                for (x in searchSpace.a.x..searchSpace.b.x) {
                    activate(x, y, z, w, space, newSpace, includeW)
                }
            }
        }
    }
    return newSpace
}

private fun activate(
        x: Int,
        y: Int,
        z: Int,
        w: Int,
        space: Map<Coord, Boolean>,
        newSpace: HashMap<Coord, Boolean>,
        includeW: Boolean = false) {
    val c = Coord(x, y, z, w)
    val actives = activeNeighbors(space, c, includeW)
    if (space.getOrDefault(c, false) && (2..3).contains(actives)
        || !space.getOrDefault(c, false) && actives == 3) {
        newSpace[c] = true
    }
    else if (newSpace.containsKey(c)) {
        newSpace[c] = false
    }
}

fun activeNeighbors(space: Map<Coord, Boolean>, c: Coord, includeW: Boolean = false): Int {
    var result = 0
    for (w in (if (includeW) -1..1 else 0..0)) {
        for (z in -1..1) {
            for (y in -1..1) {
                for (x in -1..1) {
                    val toCheck = Coord(c.x + x, c.y + y, c.z + z, c.w + w)
                    if (space.getOrDefault(toCheck, false) && toCheck != c) {
                        ++result
                    }
                }
            }
        }
    }
    return result
}

fun debug3d(space: Map<Coord, Boolean>) {
    val zs = space.keys.map { it.z }
    val ys = space.keys.map { it.y }
    val xs = space.keys.map { it.x }
    val minz = zs.minOrNull()!!
    val maxz = zs.maxOrNull()!!
    val miny = ys.minOrNull()!!
    val maxy = ys.maxOrNull()!!
    val minx = xs.minOrNull()!!
    val maxx = xs.maxOrNull()!!
    for (z in minz..maxz) {
        println("z=$z")
        for (y in miny..maxy) {
            var line = ""
            for (x in minx..maxx) {
                line += if (space.getOrDefault(Coord(x, y, z), false)) "#" else "."
            }
            println(line)
        }
        println()
    }
}

data class Coord(val x: Int, val y: Int, val z: Int, val w: Int = 0)

data class SearchSpace(val a: Coord, val b: Coord) {
    fun grow3d(): SearchSpace {
        return SearchSpace(Coord(a.x - 1, a.y - 1, a.z - 1),
            Coord(b.x + 1, b.y + 1, b.z + 1))
    }

    fun grow4d(): SearchSpace {
        return SearchSpace(Coord(a.x - 1, a.y - 1, a.z - 1, a.w - 1),
            Coord(b.x + 1, b.y + 1, b.z + 1, b.w + 1))
    }
}

