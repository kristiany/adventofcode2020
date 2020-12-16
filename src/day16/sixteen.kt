package day16

import forLinesIn

fun main() {
    forLinesIn("day16/input.txt") {
        val data = parse(it)

        val errorRate = data.tickets
            .flatMap { it.toList() }
            .filter { nr -> data.rules.map { it.valid(nr) }.all { !it } }
            .sum()
        println("Part 1: error rate $errorRate")

        val validTickets = data.tickets.filter { ticket -> anyValid(ticket, data.rules) }.plus(data.myTicket)

        // Find "weights" for each nr in the ticket, basically 0 or 1 so a bit redundant
        val weights = ArrayList<List<IntArray>>()
        for (ticket in validTickets) {
            val ticketWeights = ArrayList<IntArray>()
            for (nr in ticket) {
                val nrWeights = IntArray(ticket.size) { 0 }
                for (i in validRulesIndices(nr, data.rules)) {
                    nrWeights[i]++
                }
                ticketWeights.add(nrWeights)
            }
            //println("Ticket ${ticket.toList()}, w: ${ticketWeights.toList().map { it.toList() }}")
            weights.add(ticketWeights)
        }

        // Find all possible rules per number
        val rulesPerNr = ArrayList<RulesPerNr>()
        for (nri in data.rules.indices) {
            val possibleRules = ArrayList<Int>()
            for (ruleIndex in data.rules.indices) {
                val column = IntArray(validTickets.size)
                for (ticketIndex in validTickets.indices) {
                    column[ticketIndex] = weights[ticketIndex][nri][ruleIndex]
                }
                if (column.all { it > 0 }) {
                    possibleRules.add(ruleIndex)
                }
            }
            rulesPerNr.add(RulesPerNr(possibleRules, nri))
        }

        // Find the correct rule for each number, selecting the one with less possibilities first
        rulesPerNr.sortBy { r -> r.rules.size }
        println(rulesPerNr)
        val nrRule = IntArray(data.rules.size)
        val usedRules = HashSet<Int>()
        for (possibleRules in rulesPerNr) {
            val notUsedIndex = possibleRules.rules.minus(usedRules)[0]
            nrRule[possibleRules.nr] = notUsedIndex
            usedRules.add(notUsedIndex)
        }

        // Multiply the departure numbers in my ticket
        println(nrRule.toList())
        val ruleToNrIndex = nrRule.mapIndexed { i, rnr -> rnr to i }.toMap()
        var departures = data.rules.mapIndexed { i, rule ->
            if (rule.name.startsWith("departure")) data.myTicket[ruleToNrIndex[i]!!].toLong() else 1L
        }.reduce { acc, nr -> acc * nr }

        println("Part 2: $departures")
    }
}

fun anyValid(ticket: IntArray, rules: List<Rule>): Boolean {
    return ticket.map { nr -> rules.map { it.valid(nr) }.any { it } }
        .all { it }
}

fun validRulesIndices(nr: Int, rules: List<Rule>): List<Int> {
    val result = ArrayList<Int>()
    var i = 0
    for (rule in rules) {
        if (rule.valid(nr)) {
            result.add(i)
        }
        ++i
    }
    return result
}

fun parse(input: Sequence<String>): Data {

    val rules = ArrayList<Rule>()
    val it = input.iterator()
    var line = it.next()
    while (it.hasNext() && line.isNotBlank()) {
        val s = line.split(":")
        val name = s[0].trim()
        val ranges = s[1].split(" or ")
        val asplit = ranges[0].split("-")
        val a = asplit[0].trim().toInt()..asplit[1].trim().toInt()
        val bsplit = ranges[1].split("-")
        val b = bsplit[0].trim().toInt()..bsplit[1].trim().toInt()
        rules.add(Rule(name, a, b))
        line = it.next()
    }
    it.next() // Skip header
    val myticket = it.next().split(",").map { it.toInt() }.toIntArray()

    it.next() // Skip blank line
    it.next() // Skip header

    val tickets = ArrayList<IntArray>()
    while (it.hasNext()) {
        tickets.add(it.next().split(",").map { it.toInt() }.toIntArray())
    }

    return Data(rules, myticket, tickets)
}

data class Rule(val name: String, val a: IntRange, val b: IntRange) {
    fun valid(nr: Int): Boolean {
        return a.contains(nr) || b.contains(nr)
    }
}

data class Data(val rules: List<Rule>, val myTicket: IntArray, val tickets: List<IntArray>)

data class RulesPerNr(val rules: List<Int>, val nr: Int)

