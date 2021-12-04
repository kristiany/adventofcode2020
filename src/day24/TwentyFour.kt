package day24

import forLinesIn

fun main() {
    forLinesIn("day24/input.txt") {
        val parsed = it.map { parseDirs(it) }.toList()
        val tiles = flip(parsed)
        println("Part 1: ${tiles.values.count { it.side }} black tiles")

        var flipped = tiles.map { it.key to it.value.side }.toMap()
        //debug(flipped)
        for (i in 1..100) {
            flipped = artFlip(flipped)
            //debug(flipped)
            println("Day $i: ${flipped.values.count { it }}")
        }
    }
}

fun debug(flipped: Map<Pair<Int, Int>, Boolean>) {
    val keysOfBlacks = flipped.filterValues { it }.keys
    println("Keys of black $keysOfBlacks")
    val minx = keysOfBlacks.minOf { it.first }
    val maxx = keysOfBlacks.maxOf { it.first }
    val miny = keysOfBlacks.minOf { it.second }
    val maxy = keysOfBlacks.maxOf { it.second }
    for (y in maxy downTo miny) {
        var row = ""
        for (x in minx .. maxx) {
            if (x == 0 && y == 0) {
                row += if (flipped[Pair(x, y)] == true) 'X' else 'O'
                continue
            }
            row += if (flipped[Pair(x, y)] == true) '#' else '.'
        }
        println(row)
    }
}

fun artFlip(flipped: Map<Pair<Int, Int>, Boolean>): Map<Pair<Int, Int>, Boolean> {
    val keysOfBlacks = flipped.filterValues { it }.keys
    val minx = keysOfBlacks.minOf { it.first } - 1
    val maxx = keysOfBlacks.maxOf { it.first } + 1
    val miny = keysOfBlacks.minOf { it.second } - 1
    val maxy = keysOfBlacks.maxOf { it.second } + 1
    val copy = HashMap(flipped.toMap())
    for (y in maxy downTo miny) {
        for (x in minx..maxx) {
            doFlip(flipped, Pair(x, y), copy)
        }
    }
    return copy
}

private fun doFlip(flipped: Map<Pair<Int, Int>, Boolean>,
                   pos: Pair<Int, Int>,
                   copy: HashMap<Pair<Int, Int>, Boolean>) {
    val tileSide = flipped[pos] ?: false
    val adjacents = adjacents(pos, flipped, copy)
    val black = adjacents.count { it }
    if (tileSide && (black == 0 || black > 2)) {
        //println("$pos Tile is black, and adj blacks are $black: setting to white")
        copy[pos] = false
    } else if ((! tileSide) && black == 2) {
        //println("$pos Tile is white, and adj blacks are $black: setting to black")
        copy[pos] = true
    }
}

fun adjacents(pos: Pair<Int, Int>,
              flipped: Map<Pair<Int, Int>, Boolean>,
              copy: HashMap<Pair<Int, Int>, Boolean>): List<Boolean> {
    return orientations.map {
        val orientedPos = pos(pos, it)
        if (copy[orientedPos] == null) {
            copy[orientedPos] = false
        }
        flipped[orientedPos] ?: false
    }.toList()
}

private const val ne = "ne"
private const val nw = "nw"
private const val se = "se"
private const val sw = "sw"
private const val e = "e"
private const val w = "w"
private val orientations = listOf(ne, nw, se, sw, e, w)
// Using the skewed-axis coord system
private val posSteps = mapOf(
    ne to Pair(0, 1),
    nw to Pair(-1, 1),
    se to Pair(1, -1),
    sw to Pair(0, -1),
    e to Pair(1, 0),
    w to Pair(-1, 0),
)

fun parseDirs(dirs: String): List<String> {
    val parsed = ArrayList<String>()
    var tail = dirs
    while (tail.isNotEmpty()) {
        for (orientation in orientations) {
            if (tail.startsWith(orientation)) {
                parsed.add(orientation)
                tail = tail.removePrefix(orientation)
                break
            }
        }
    }
    return parsed
}

fun flip(flips: List<List<String>>): HashMap<Pair<Int, Int>, Tile> {
    val root = Tile(Pair(0, 0))
    val tiles = HashMap<Pair<Int, Int>, Tile>()
    tiles[Pair(0, 0)] = root
    for (flip in flips) {
        go(flip, root, tiles)
    }
    return tiles
}

fun go(dirs: List<String>, fromTile: Tile, tiles: HashMap<Pair<Int, Int>, Tile>) {
    if (dirs.isEmpty()) {
        fromTile.side = !fromTile.side
        return
    }
    val move = dirs.first()
    val tail = dirs.subList(1, dirs.size)
    if (fromTile.nodes[move] == null) {
        val moveToPos = pos(fromTile.pos, move)
        val moveTo = tiles[moveToPos] ?: run {
            val t = Tile(moveToPos)
            tiles[moveToPos] = t
            t
        }
        fromTile.nodes[move] = moveTo

    }
    go(tail, fromTile.nodes[move]!!, tiles)
}

fun pos(fromPos: Pair<Int, Int>, move: String): Pair<Int, Int> {
    val moveToX = fromPos.first + posSteps[move]!!.first
    val movetoY = fromPos.second + posSteps[move]!!.second
    return Pair(moveToX, movetoY)
}

data class Tile(val pos: Pair<Int, Int>, val nodes: HashMap<String, Tile> = HashMap(), var side: Boolean = false)
