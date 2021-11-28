package day21

import forLinesIn

fun main() {
    forLinesIn("day21/input.txt") {
        val foods = parse(it.toList())
        println(foods)

        val allAllgs = foods.flatMap { it.allergens }.toSet()
        val allIngrs = foods.flatMap { it.ingredients }.toSet()
        val allgMap = allAllgs.associateWith { allg ->
            foods.filter { it.allergens.contains(allg) }
                .map { it.ingredients }
                .reduce { a, b -> a.intersect(b) }
        }
        val nonAllgs = allIngrs.minus(allgMap.values.flatten().toSet())
        println(nonAllgs)
        val count = nonAllgs.sumOf { nonAllg -> foods.count { it.ingredients.contains(nonAllg) } }
        println("Part 1: $count")

        println(allgMap)
        var singleIngrMap = allgMap
        while (singleIngrMap.values.any { it.size > 1 }) {
            val singles = singleIngrMap.entries
                .filter { it.value.size == 1 }
                .map { it.value.first() }
                .toSet()
            //println(singles)
            singleIngrMap = singleIngrMap.entries.associate {
                if (it.value.size > 1) {
                    it.key to it.value.minus(singles)
                } else {
                    it.key to it.value
                }
            }
            println(singleIngrMap)
        }
        val list = singleIngrMap.keys.sorted().joinToString(",") { singleIngrMap[it]!!.first() }
        println("Part 2: $list")
    }
}

fun parse(input: List<String>): List<Food> {
    return input.map {
        val ingrsVsAllg = it.split("(")
        val ingrs = ingrsVsAllg[0].trim().split(" ").toSet()
        val allg = ingrsVsAllg[1].replace(")", "")
            .replace("contains", "")
            .trim().split(",").map { it.trim() }
            .toSet()
        Food(ingrs, allg)
    }.toList()
}

data class Food(val ingredients: Set<String>, val allergens: Set<String>)