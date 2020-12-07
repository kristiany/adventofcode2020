package day07

import forLinesIn

fun main() {
    forLinesIn("day07/input.txt") { input ->
        val rules = input.map {
            val parent = it.split("bags")[0].trim()
            val contains = it.split("contain")[1].split(",")
                .filter { !it.contains("no other bags") }
                .map {
                    val words = it.trim().split(" ")
                    val count = words[0].toInt()
                    val name = "${words[1]} ${words[2]}"
                    Contains(name, count)
                }
            parent to contains
        }.toMap()

        val toParent = HashMap<String, HashSet<String>>()
        for (e in rules) {
            for (contain in e.value) {
                if (!toParent.containsKey(contain.name)) {
                    toParent[contain.name] = HashSet()
                }
                toParent[contain.name]?.add(e.key)
            }
        }

        val colors = colors(toParent, "shiny gold", HashSet())
        println("Part 1: colors ${colors.size - 1}")  // Not counting shiny gold
        val count = countBags(rules, "shiny gold")
        println("Part 2: bags ${count - 1}")  // Not counting shiny gold
    }
}

fun colors(toParent: Map<String, Set<String>>, name: String, acc: HashSet<String>): Set<String> {
    acc.add(name)
    if (!toParent.containsKey(name)) {
        return acc
    }
    toParent[name]!!.forEach { colors(toParent, it, acc) }
    return acc
}

fun countBags(rules: Map<String, List<Contains>>, name: String): Int {
    if (!rules.containsKey(name)) {
        return 1
    }
    return 1 + rules[name]!!.map { contain -> contain.count * countBags(rules, contain.name) }.sum()
}


data class Contains(val name: String, val count: Int)