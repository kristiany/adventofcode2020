package day20

import forLinesIn
import java.lang.IllegalArgumentException
import java.lang.Math.abs
import java.util.Stack
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {
    forLinesIn("day20/input.txt") {
        val tiles = parse(it.toList())
        val tileMatches = getMatches(tiles)
        val answer1 = tileMatches.filter { it.size <= 2 }
            .map { it[0].compareToId.toLong() }
            .reduce { a, b -> a * b }
        println("Part 1: $answer1")

        val cornerMatches = tileMatches.first { it.size <= 2 }
        val cornerTile = tiles.find { it.id == cornerMatches[0].compareToId }!!
        val map = matchMap(tiles, cornerTile)
        debug(map)

        val strippedMap = strippedMap(map)
        //debug(strippedMap)

        var rotatedMap = strippedMap
        var monsterCount = 0
        for (rotation in 0..3) {
            //debug(strippedMap)
            var currentCount = countSeaMonsters(rotatedMap)
            if (currentCount > monsterCount) {
                monsterCount = currentCount
            }
            val flippedMap = rotatedMap.map { it.reversed() }
            //debug(flippedMap)
            currentCount = countSeaMonsters(flippedMap)
            if (currentCount > monsterCount) {
                monsterCount = currentCount
            }
            rotatedMap = rotateRight(rotatedMap)
        }
        println("Found $monsterCount sea monsters!")
        println("Part 2: ${rotatedMap.sumOf { it.count { it }} - monsterCount * 15}")

    }

}

private fun countSeaMonsters(map: List<List<Boolean>>): Int {
    var result = 0
    for (y in 0 until map.size) {
        for (x in 0 until map[0].size) {
            if (match(map, x, y)) {
                println("Sea monster at x: $x, y: $y")
                result++
            }
        }
    }
    return result
}

private fun rotateRight(map: List<List<Boolean>>): List<List<Boolean>> {
    val result = ArrayList<List<Boolean>>()
    for (x in 0 until map[0].size) {
        val row = ArrayList<Boolean>()
        for (y in (map.size - 1) downTo 0) {
            row.add(map[y][x])
        }
        result.add(row)
    }
    return result
}

private fun match(map: List<List<Boolean>>, x: Int, y: Int): Boolean {

    if (x + 19 >= map[y].size
        || y - 1 < 0
        || y + 1 >= map.size) {
        return false
    }
    /*     x=0
     y=-1                    #
     y=0   #    ##    ##    ###
     y=1    #  #  #  #  #  #
     */
    return map[y][x]
            && map[y + 1][x + 1]
            && map[y + 1][x + 4]
            && map[y][x + 5]
            && map[y][x + 6]
            && map[y + 1][x + 7]
            && map[y + 1][x + 10]
            && map[y][x + 11]
            && map[y][x + 12]
            && map[y + 1][x + 13]
            && map[y + 1][x + 16]
            && map[y][x + 17]
            && map[y][x + 18]
            && map[y][x + 19]
            && map[y - 1][x + 18]
}

private fun strippedMap(map: List<PositionedTile>): List<List<Boolean>> {
    val sorted = map.sortedWith(compareBy<PositionedTile> { it.pos.second }.thenComparingInt { it.pos.first })
    val ymin = sorted.minOf { it.pos.second }
    val ymax = sorted.maxOf { it.pos.second }
    val ysize = abs(ymax - ymin) + 1
    val strippedMap = ArrayList<List<Boolean>>()
    for (tileRow in sorted.chunked(ysize)) {
        val stripped = tileRow.map { stripEdges(it) }
        for (y in 0 until stripped[0][0].size) {
            val strippedRow = ArrayList<Boolean>()
            for (tile in stripped) {
                strippedRow.addAll(tile[y])
            }
            strippedMap.add(strippedRow)
        }
    }
    return strippedMap
}

fun stripEdges(tile: PositionedTile): List<List<Boolean>> {
    val result = ArrayList<List<Boolean>>()
    for (row in tile.tile.data.subList(1, tile.tile.data.size - 1)) {
        result.add(row.subList(1, row.size - 1))
    }
    return result
}

fun debug(map: List<PositionedTile>) {
    val sorted = map.sortedWith(compareBy<PositionedTile> { it.pos.second }.thenComparingInt { it.pos.first })
    val ymin = sorted.minOf { it.pos.second }
    val ymax = sorted.maxOf { it.pos.second }
    val ysize = abs(ymax - ymin) + 1
    for (row in sorted.chunked(ysize)) {
        row.forEach {
            print("${it.tile.id}: ${it.pos}  ")
        }
        println()
    }
    val debugMap = ArrayList<String>()
    for (tileRow in sorted.chunked(ysize)) {
        val data = tileRow.map { it.tile.data }
        for (y in 0 until data[0][0].size) {
            val row = ArrayList<String>()
            for (tile in data) {
                row.add(tile[y].map { if (it) '#' else '.' }.joinToString(""))
            }
            debugMap.add(row.joinToString(" "))
        }
        debugMap.add("\n")
    }
    debugMap.onEach { println(it) }
}

fun debugTile(tile: Tile) {
    for (row in tile.data) {
        println(row.map { if (it) '#' else '.' }.joinToString(""))
    }
}

fun rotateAndFlipTo(match: Match,
                    tile: Tile,
                    fromPos: Pair<Int, Int>): PositionedTile {
    val toMatchEdge = match.compareToEdge
    val withEdge = match.withEdge
    val newEdge = (toMatchEdge + /* opposite edge */ 2 + /* positive % */ 4) % 4
    val rotation = newEdge - withEdge
    //println(match)
    //println("Compare edge $toMatchEdge, matched edge $withEdge, translated wanted edge $newEdge, rotation $rotation, flipped: ${match.flipped}")
    val data = ArrayList<List<Boolean>>()
    // Rotation negates the flip, since comparison is on rotated edges
    val finalFlip = ! match.flipped
    //println("Do flip: $doFlip")
    when (rotation) {
        0 -> {
            // No need to rotate
            for (row in tile.data) {
                data.add(ArrayList(row))
            }
        }
        -1, 3 -> {
            for (x in 9 downTo 0) {
                val row = ArrayList<Boolean>()
                for (y in 0..9) {
                    row.add(tile.data[y][x])
                }
                data.add(row)
            }
        }
        1, -3 -> {
            for (x in 0..9) {
                val row = ArrayList<Boolean>()
                for (y in 9 downTo 0) {
                    row.add(tile.data[y][x])
                }
                data.add(row)
            }
        }
        -2, 2 -> {
            for (row in tile.data.reversed()) {
                data.add(row.reversed())
            }
        }
    }
    val flippedData = flip(data, finalFlip, newEdge)

    return PositionedTile(Tile(tile.id, flippedData, finalFlip), toPos(toMatchEdge, fromPos))
}

private fun flip(data: List<List<Boolean>>,
                 flipped: Boolean,
                 edge: Int): List<List<Boolean>> {
    var flippedData = data
    if (flipped) {
        if (edge == 0 || edge == 2) {
            //flip x-wise
            flippedData = ArrayList()
            for (row in data) {
                flippedData.add(row.reversed())
            }
        } else {
            // flip y-wise
            flippedData = data.reversed()
        }
    }
    return flippedData
}

fun toPos(fromEdge: Int, fromPos: Pair<Int, Int>): Pair<Int, Int> {
    when (fromEdge) {
        0 -> return Pair(fromPos.first, fromPos.second - 1)
        1 -> return Pair(fromPos.first + 1, fromPos.second)
        2 -> return Pair(fromPos.first, fromPos.second + 1)
        3 -> return Pair(fromPos.first - 1, fromPos.second)
    }
    throw IllegalArgumentException("Invalid edge $fromEdge")
}

private fun matchMap(tiles: List<Tile>, cornerTile: Tile): List<PositionedTile> {
    val idToTiles = tiles.associateBy { it.id }

    val map = ArrayList<PositionedTile>()
    map.add(PositionedTile(cornerTile, Pair(0, 0)))
    val visited = HashSet<Int>()
    val next = Stack<Int>()
    next.add(cornerTile.id)

    println("${cornerTile.id} Start tile")
    debugTile(cornerTile)

    while (map.size < tiles.size && next.isNotEmpty()) {
        val compareToId = next.pop()
        val searchSpace = idToTiles.keys.minus(visited).minus(compareToId)
        val compareToPositioned = map.find { it.tile.id == compareToId }!!
        val compareTo = compareToPositioned.tile
        val fromPos = compareToPositioned.pos
        for (id in searchSpace) {
            val with = idToTiles[id]!!
            val matchesOnCompareTo = compare(compareTo, with)
            if (matchesOnCompareTo.isNotEmpty()) {
                val match = matchesOnCompareTo.first() // Can just be one
                if (next.contains(match.withId)) {
                    continue // Let's skip refs to tiles already in the to-be processed queue
                }
                println("Connect ${compareTo.id} with ${with.id} from pos $fromPos")
                val positioned = rotateAndFlipTo(
                    match,
                    with,
                    fromPos
                )
                map.add(positioned)
                next.add(with.id)

                /*println("${with.id} Before")
                debugTile(with)
                println("${with.id} After")
                debugTile(positioned.tile)*/
            }
        }
        visited.add(compareTo.id)
    }
    return map

}


private fun getMatches(tiles: List<Tile>): ArrayList<List<Match>> {
    val tileMatches = ArrayList<List<Match>>()
    for (i in tiles.indices) {
        val compareTo = tiles[i]
        val matchesOnCompareTo = tiles.filter { it != compareTo }
            .flatMap { compare(compareTo, it) }
        if (matchesOnCompareTo.isNotEmpty()) {
            tileMatches.add(matchesOnCompareTo)
        }
    }
    return tileMatches
}

fun compare(compareTo: Tile, with: Tile): List<Match> {
    val result = ArrayList<Match>()
    for (e in 0..3) {
        val current = compareTo.edge(e)
        for (e2 in 0..3) {
            val withEdge = with.edge(e2)
            if (current == withEdge) {
                result.add(Match(compareTo.id, e, with.id, e2, false))
            }
            else if (current == withEdge.reversed()) {
                result.add(Match(compareTo.id, e, with.id, e2, true))
            }
        }
    }
    return result
}


fun parse(input: List<String>): List<Tile> {
    val result = ArrayList<Tile>()
    val iter = input.listIterator()
    while (iter.hasNext()) {
        val line = iter.next()
        if (line.startsWith("Tile")) {
            val id = line.split(" ")[1].replace(":", "").toInt()
            val data = ArrayList<List<Boolean>>()
            for (i in 0..9) {
                val dataLine = iter.next().trim()
                data.add(dataLine.map { it == '#' }.toList())
            }
            result.add(Tile(id, data, false))
        }
    }
    return result
}

data class PositionedTile(val tile: Tile, val pos: Pair<Int, Int>)

data class Tile(val id: Int,
                val data: List<List<Boolean>>,
                val flipped: Boolean) {

    /*
            0
         -------
         |     |
       3 |     | 1
         |     |
         -------
            2
     */
    fun edge(nr: Int): List<Boolean> {
        when (nr) {
            // Fetch edge by rotation order
            0 -> return data[0]
            1 -> return data.map { it[9] }.toList()
            2 -> return data[9].reversed()
            3 -> return data.map { it[0] }.toList().reversed()
        }
        throw IllegalArgumentException("Nah ah, $nr is not a valid edge nr")
    }
}

data class Match(val compareToId: Int, val compareToEdge: Int, val withId: Int, val withEdge: Int, val flipped: Boolean) {
    init {
        require((0..3).contains(compareToEdge) && (0..3).contains(withEdge)) {
            "Edges mut be between 0-3, was compare to: $compareToEdge, with: $withEdge"
        }
    }
}
