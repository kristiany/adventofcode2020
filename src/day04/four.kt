package day04

import forLinesIn

val validEyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
val validPidPattern = Regex("\\d{9}")
val validHairColorPattern = Regex("#[0-9a-f]{6}")


fun main() {
    forLinesIn("day04/input.txt") {
        val pps = parse(it)
        val validProps = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        val valid1 = pps.count { p -> p.keys.containsAll(validProps) }
        println("Part 1: valid ${valid1}")

        val valid2 = pps.count { p -> validate(p, validProps) }
        println("Part 2: valid ${valid2}")
    }
}

private fun validate(p: Map<String, String>, validProps: Set<String>): Boolean {
    return p.keys.containsAll(validProps)
            && validNr(p["byr"], 1920, 2002)
            && validNr(p["iyr"], 2010, 2020)
            && validNr(p["eyr"], 2020, 2030)
            && validHeight(p["hgt"])
            && validHair(p["hcl"])
            && validEyeColors.contains(p["ecl"])
            && validPid(p["pid"])
}

fun validPid(s: String?): Boolean {
    return s != null && s.matches(validPidPattern)
}

fun validHair(s: String?): Boolean {
    return s != null && s.matches(validHairColorPattern)
}

fun validHeight(s: String?): Boolean {
    if (s != null && s.contains("in")) {
        return validNr(s.replace("in", ""), 59, 76)
    }
    if (s != null && s.contains("cm")) {
        return validNr(s.replace("cm", ""), 150, 193)
    }
    return false
}

fun validNr(s: String?, lower: Int, upper: Int): Boolean {
    val nr = s?.toInt()
    return nr != null && lower <= nr && nr <= upper
}

private fun parse(it: Sequence<String>): ArrayList<Map<String, String>> {
    val pps = ArrayList<Map<String, String>>()
    var buffer = ""
    for (line in it) {
        if (line.isBlank()) {
            pps.add(parse(buffer))
            buffer = ""
        } else {
            buffer += " $line"
        }
    }
    pps.add(parse(buffer))
    return pps
}

fun parse(buffer: String): Map<String, String> {
    return buffer.trim().split(" ")
        .map {
            val kv = it.split(":")
            kv[0] to kv[1]
        }.toMap()
}
