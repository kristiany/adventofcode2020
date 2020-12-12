package day12

import forLinesIn
import kotlin.math.absoluteValue

fun main() {
    forLinesIn("day12/input.txt") {
        val nav = it.map { ins ->
            val trimmed = ins.trim()
            Waypoint(trimmed[0], trimmed.substring(1).toInt())
        }.toList()

        println(nav)
        val (x, y) = part1Navigate(nav)
        println("Part 1: x=$x, y=$y, manhattan distance ${x.absoluteValue + y.absoluteValue}")

        val pos = part2Navigate(nav)
        println("Part 2: x=${pos.x}, y=${pos.y}, manhattan distance ${pos.manhattanDistance()}")

    }
}

fun part1Navigate(nav: List<Waypoint>): Pair<Int, Int> {
    var x = 0
    var y = 0
    // 0 = W, 1 = N, 2 = E, 3 = S
    var facing = 2
    for (n in nav) {
        //println("x=$x, y=$y")
        //println("Facing: $facing")
        //println(n)
        when (n.ins) {
            'N' -> {
                y -= n.value
            }
            'S' -> {
                y += n.value
            }
            'E' -> {
                x += n.value
            }
            'W' -> {
                x -= n.value
            }
            'L' -> {
                // Java handles negative numbers modulo by keeping sign
                // https://torstencurdt.com/tech/posts/modulo-of-negative-numbers/
                val f = (facing - n.value / 90) % 4
                facing = if (f < 0) f + 4 else f
            }
            'R' -> {
                facing = (facing + n.value / 90) % 4
            }
            'F' -> {
                when (facing) {
                    0 -> x -= n.value
                    1 -> y -= n.value
                    2 -> x += n.value
                    3 -> y += n.value
                }
            }
        }
    }
    return Pair(x, y)
}

fun part2Navigate(nav: List<Waypoint>): Pos {
    var ship = Pos(0, 0)
    var wp = Vec(10, -1)
    for (n in nav) {
        //println("Ship $ship")
        //println("Waypoint $wp")
        //println(n)
        when (n.ins) {
            'N' -> {
                wp = wp.addy(-n.value)
            }
            'S' -> {
                wp = wp.addy(n.value)
            }
            'E' -> {
                wp = wp.addx(n.value)
            }
            'W' -> {
                wp = wp.addx(-n.value)
            }
            'L' -> {
                wp = wp.rotateCCW(n.value)
            }
            'R' -> {
                wp = wp.rotateCW(n.value)
            }
            'F' -> {
                ship = ship.add(wp.mul(n.value))
            }
        }
    }
    return ship
}

data class Waypoint(val ins: Char, val value: Int)

class Pos(val x: Int, val y: Int) {
    fun add(v: Vec): Pos {
        return Pos(x + v.x, y + v.y)
    }

    fun manhattanDistance(): Int {
        return x.absoluteValue + y.absoluteValue
    }

    override fun toString(): String {
        return "Pos(x=$x, y=$y)"
    }

}

class Vec(val x: Int, val y: Int) {
    fun mul(t: Int): Vec {
        return Vec(x * t, y * t)
    }

    fun addx(addx: Int): Vec {
        return Vec(x + addx, y)
    }

    fun addy(addy: Int): Vec {
        return Vec(x, y + addy)
    }

    fun rotateCW(degrees: Int): Vec {
        var r = this
        for (t in 1..(degrees / 90)) {
            r = r.rotateClockwise()
        }
        return r
    }

    fun rotateCCW(degrees: Int): Vec {
        var r = this
        for (t in 1..(degrees / 90)) {
            r = r.rotateCounterClockwise()
        }
        return r
    }

    private fun rotateClockwise(): Vec {
        return Vec(-y, x)
    }

    private fun rotateCounterClockwise(): Vec {
        return Vec(y, -x)
    }

    override fun toString(): String {
        return "Vec(x=$x, y=$y)"
    }
}