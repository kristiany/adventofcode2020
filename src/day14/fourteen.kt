package day14

import forLinesIn
import java.util.BitSet
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow


fun main() {
    forLinesIn("day14/input.txt") {
        val programs = parse(it)
        //println(programs)

        val mem = runV1(programs)
        println("Part 1: ${mem.values.sum()}")

        val mem2 = runV2(programs)
        println("Part 2: ${mem2.values.sum()}")
    }
}

private fun runV1(programs: List<Program>): HashMap<Long, Long> {
    val mem = HashMap<Long, Long>()
    programs.forEach { p ->
        p.mem.forEach { ins ->
            mem[ins.position] = p.applyMaskToValue(ins.value)
        }
    }
    return mem
}

private fun runV2(programs: List<Program>): HashMap<Long, Long> {
    val mem = HashMap<Long, Long>()
    programs.forEach { p ->
        p.mem.forEach { ins ->
            p.applyMaskToPosition(ins.position).forEach {
                mem[it] = ins.value
            }

        }
    }
    return mem
}

fun parse(input: Sequence<String>): List<Program> {
    val result = ArrayList<Program>()
    var mask = ""
    var mem = ArrayList<Ins>()
    for (line in input) {
        if (line.startsWith("mask")) {
            if (mask.isNotBlank()) {
                result.add(Program(mask.reversed(), mem))
                mem = ArrayList()
            }
            mask = line.split("=")[1].trim()
        }
        else if (line.startsWith("mem")) {
            val position = line.split("[")[1].split("]")[0].toLong()
            val value = line.split("=")[1].trim().toLong()
            mem.add(Ins(position, value))
        }
    }
    result.add(Program(mask.reversed(), mem))
    return result
}

data class Program(val mask: String, val mem: List<Ins>) {
    fun applyMaskToValue(value: Long): Long {
        val set = BitSet.valueOf(longArrayOf(value))
        mask.forEachIndexed{ i, c ->
            if (c != 'X') {
                set.set(i, c == '1')
            }
        }
        return set.toLongArray()[0]
    }

    fun applyMaskToPosition(value: Long): List<Long> {
        val set = BitSet.valueOf(longArrayOf(value))
        val variables = ArrayList<Int>()
        mask.forEachIndexed{ i, c ->
            if (c == 'X') {
                variables.add(i)
            } else if (c == '1') {
                set.set(i)
            }
        }

        // Utilize the bit combinations of adjacent bits for consecutive numbers
        val combos = ArrayList<BooleanArray>()
        for (n in 0 until (2.0.pow(variables.size).toLong())) {
            //println("n: $n, ${n.toString(2)}")
            val bitSet = BitSet.valueOf(longArrayOf(n))
            val b = BooleanArray(variables.size)
            for (j in 0 until variables.size) {
                b[j] = bitSet.get(j)
            }
            combos.add(b)
        }
        //println(combos.map { it.toList() })
        //println(variables)

        val base = set.toLongArray()[0]
        val positions = ArrayList<Long>()
        for (combo in combos) {
            val variant = BitSet.valueOf(longArrayOf(base))
            for (c in combo.zip(variables)) {
                variant.set(c.second, c.first)
            }
            positions.add(variant.toLongArray()[0])
        }
        return positions
    }

}

data class Ins(val position: Long, val value: Long)