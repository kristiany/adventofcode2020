package day19

import forLinesIn

fun main() {
    forLinesIn("day19/input.txt") {
        val parcel = parse(it.toList())

        val regex1 = Regex(createRegex(parcel.rulesMap, 0))
        val matching1 = parcel.messages.map { it.matches(regex1) }
        println("Part 1: matching ${matching1.count { it }}")

        // Pumping lemma, what a thing
        // > 11: 42 31 | 42 42 31 31 | 42 42 42 31 31 31 | 42 42 42 42 31 31 31 31 | 42 42 42 42 42 31 31 31 31 31
        // > and 8: 42 | 42 42 | 42 42 42 | 42 42 42 42 | 42 42 42 42 42 did the trick.
        // > I just kept applying the pumping lemma until it stopped matching new rows.
        // https://www.reddit.com/r/adventofcode/comments/kg82yl/2020_day_19/ggdgsx7/?utm_source=reddit&utm_medium=web2x&context=3
        val rule8 = (1..30).map { IntArray(it) { 42 }.toList() }
        val rule11 = (1..30).map { (IntArray(it) { 42 } + IntArray(it) { 31 }).toList() }
        val updatedRulesMap = HashMap(parcel.rulesMap)
        updatedRulesMap[8] = Rule(8, null, rule8)
        updatedRulesMap[11] = Rule(11, null, rule11)

        val regex2 = Regex(createRegex(updatedRulesMap, 0))
        val matching2 = parcel.messages.map { it.matches(regex2) }
        println("Part 2: matching ${matching2.count { it }}")
    }
}

// Inspired by https://0xdf.gitlab.io/adventofcode2020/19
fun createRegex(rulesMap: Map<Int, Rule>, index: Int): String {
    val rule = rulesMap[index]!!
    if (rule.value != null) {
        return rule.value
    }
    return "(${(rule.grammars!!.map { exactGrammar ->
        exactGrammar.joinToString("") { rule ->
            createRegex(rulesMap, rule)
        }
    }).joinToString("|")})"
}

fun parse(input: List<String>): Parcel {
    var parsingRules = true
    val ruleMap: HashMap<Int, Rule> = HashMap()
    val messages: ArrayList<String> = ArrayList()
    for (line in input) {
        if (line.isBlank()) {
            parsingRules = false
            continue
        } else if (parsingRules) {
            val colonSplit = line.split(":")
            val index = colonSplit[0].trim().toInt()
            var value: String?
            if (colonSplit[1].contains("\"")) {
                value = colonSplit[1].split("\"")[1]
                ruleMap[index] = Rule(index, value)
            } else {
                val pipeSplit = colonSplit[1].split("|")
                val left = toInts(pipeSplit[0])
                val rules = ArrayList<List<Int>>()
                rules.add(left)
                if (pipeSplit.size > 1) {
                    rules.add(toInts(pipeSplit[1]))
                }
                ruleMap[index] = Rule(index, rules)
            }
        } else {
            messages.add(line.trim())
        }
    }
    val root = Rules(ruleMap[0]!!)
    return Parcel(root, ruleMap, messages)
}

private fun toInts(input: String): List<Int> =
    input.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }

data class Parcel(val rules: Rules, val rulesMap: Map<Int, Rule>, val messages: List<String>)

data class Rules(val root: Rule)

data class Rule(val index: Int, val value: String?, val grammars: List<List<Int>>?) {

    constructor(index: Int, value: String) : this(index, value, null)

    constructor(index: Int, rules: List<List<Int>>) : this(index, null, rules)
}