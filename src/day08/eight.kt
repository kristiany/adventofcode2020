package day08

import forLinesIn

fun main() {
    forLinesIn("day08/input.txt") {
        val program = parse(it)
        val (ok, acc) = execute(program)
        println("Part 1: ok $ok, acc $acc")

        val switches = program.filter { "nop" == it.cmd || "jmp" == it.cmd}.map { it.seq }
        for (switch in switches) {
            val (ok, acc) = execute(program, switch)
            if (ok) {
                println("Part 2: wrong instruction at index $switch, acc $acc")
                break
            }
        }
    }
}

private fun execute(program: List<Ins>, switchIndex: Int? = null): Pair<Boolean, Long> {
    val history = HashSet<Int>()
    var acc = 0L
    var pointer = 0
    while (pointer < program.size && !history.contains(pointer)) {
        val ins = program[pointer]
        history.add(pointer)
        var cmd = ins.cmd
        if (switchIndex != null && switchIndex == pointer) {
            cmd = if ("jmp" == cmd) "nop" else "jmp"
        }
        if ("acc" == cmd) {
            acc += ins.value
            ++pointer
        } else if ("jmp" == cmd) {
            pointer += ins.value.toInt()
        } else if ("nop" == cmd) {
            ++pointer
        }
    }
    return Pair(pointer == program.size && !history.contains(pointer), acc)
}

fun parse(input: Sequence<String>): List<Ins> {
    return input.mapIndexed { i, line ->
        val sections = line.trim().split(" ")
        Ins(i, sections[0], sections[1].toLong())
    }.toList()
}

data class Ins(val seq: Int, val cmd: String, val value: Long);
