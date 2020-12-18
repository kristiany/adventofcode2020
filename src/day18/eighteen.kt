package day18

import forLinesIn
import kotlin.collections.ArrayDeque

fun main() {
    forLinesIn("day18/input.txt") {
        val test1 = "1 + 2 * 3 + 4 * 5 + 6" // 71 - 231
        val test2 = "1 + (2 * 3) + (4 * (5 + 6))" // 51 - 51
        val test3 = "2 * 3 + (4 * 5)" // 26 - 46
        val test4 = "5 + (8 * 3 + 9 + 3 * 4 * 3)" // 437 - 1445
        val test5 = "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))" // 12240 - 669060
        val test6 = "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" // 13632 - 23340
        //val input = listOf(test6)
        val input = it.toList()

        val res = input.map { eval(it, ::eval) }.sum()
        println("Part 1: sum $res")

        val res2 = input.map { eval(it, ::eval2) }.sum()
        println("Part 2: sum $res2")
    }
}

private fun eval(expr: String, eval: (ops: ArrayDeque<String>, nrs: ArrayDeque<Long>) -> Long): Long {
    val parts = expr.split(" ")
    val nrStack = ArrayDeque<Long>()
    val opStack = ArrayDeque<String>()
    val ops = setOf("+", "*")
    for (part in parts) {
        if (ops.contains(part)) {
            opStack.addLast(part)
        }
        else if(part.startsWith("(")) {
            for (i in 0 until part.count { it == '(' }) {
                opStack.addLast("(")
            }
            nrStack.addLast(part.replace("(", "").trim().toLong())
        }
        else if(part.endsWith(")")) {
            nrStack.addLast(part.replace(")", "").trim().toLong())
            for (i in 0 until part.count { it == ')' }) {
                val localNrStack = ArrayDeque<Long>()
                val localOpStack = ArrayDeque<String>()
                localNrStack.addFirst(nrStack.removeLast());
                while (opStack.last() != "(") {
                    localOpStack.addFirst(opStack.removeLast())
                    localNrStack.addFirst(nrStack.removeLast());
                }
                opStack.removeLast()
                nrStack.addLast(eval(localOpStack, localNrStack))
            }
        }
        else {
            nrStack.addLast(part.trim().toLong())
        }
    }
    return eval(opStack, nrStack)
}

fun eval(op: String, a: Long, b: Long): Long {
    if (op == "+") {
        return a + b
    }
    return a * b
}

fun eval(ops: ArrayDeque<String>, nrs: ArrayDeque<Long>): Long {
    while (ops.size > 0) {
        nrs.addFirst(eval(ops.removeFirst(), nrs.removeFirst(), nrs.removeFirst()))
    }
    return nrs.removeFirst()
}

fun eval2(ops: ArrayDeque<String>, nrs: ArrayDeque<Long>): Long {
    // Simple approach, two passes, eval + first, * seconds
    val mulNrStack = ArrayList<Long>()
    mulNrStack.add(nrs.removeFirst())
    while (ops.size > 0) {
        if (ops.removeFirst() == "+") {
            mulNrStack.add(eval("+", mulNrStack.removeLast(), nrs.removeFirst()))
        }
        else {
            mulNrStack.add(nrs.removeFirst())
        }
    }
    return mulNrStack.reduce { acc, v -> acc * v }
}